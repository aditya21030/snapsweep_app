package com.yousful.snapsweepai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yousful.snapsweepai.data.DuplicateGroup
import com.yousful.snapsweepai.data.ScreenShotItem
import com.yousful.snapsweepai.data.ScreenshotCategory
import com.yousful.snapsweepai.logic.OcrScreenshotClassifier
import com.yousful.snapsweepai.logic.OcrTextExtractor
import com.yousful.snapsweepai.logic.PerceptualHashUtil
import com.yousful.snapsweepai.logic.VisualDuplicateDetector
import com.yousful.snapsweepai.repository.ScreenshotRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ScreenshotViewModel(application: Application) : AndroidViewModel(application) {


    private val repository = ScreenshotRepository(application)


    private val _allScreenshots =
        MutableStateFlow<List<ScreenShotItem>>(emptyList())


    private val _selectedCategory =
        MutableStateFlow(ScreenshotCategory.ALL)

    val selectedCategory: StateFlow<ScreenshotCategory> = _selectedCategory

    val screenshots: StateFlow<List<ScreenShotItem>> =
        combine(_allScreenshots, _selectedCategory) { all, category ->
            if (category == ScreenshotCategory.ALL) {
                all
            } else {
                all.filter { it.category == category }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    private val _visualDuplicateGroups =
        MutableStateFlow<List<DuplicateGroup>>(emptyList())

    val visualDuplicateGroups: StateFlow<List<DuplicateGroup>> =
        _visualDuplicateGroups


    private val _isVisualScanning = MutableStateFlow(false)
    val isVisualScanning: StateFlow<Boolean> = _isVisualScanning


    private val visualHashCache = mutableMapOf<Long, Long>()
    private var hasVisualScanCompleted = false


    fun detectVisualDuplicates() {

        if (hasVisualScanCompleted) return

        viewModelScope.launch(Dispatchers.Default) {

            _isVisualScanning.value = true


            val shots =
                _allScreenshots.value
                    .filter { it.category == ScreenshotCategory.SCREENSHOTS }
                    .distinctBy { it.id }


            coroutineScope {
                shots.map { shot ->
                    async {
                        if (!visualHashCache.containsKey(shot.id)) {
                            val hash =
                                PerceptualHashUtil.computeHash(
                                    context = getApplication(),
                                    uri = shot.uri
                                )
                            if (hash != null) {
                                visualHashCache[shot.id] = hash
                            }
                        }
                    }
                }.awaitAll()
            }

            // 🔥 USE HASH CACHE
            val result =
                VisualDuplicateDetector.findVisualDuplicatesUsingCache(
                    screenshots = shots,
                    hashCache = visualHashCache
                )

            _visualDuplicateGroups.value = result
            hasVisualScanCompleted = true
            _isVisualScanning.value = false
        }
    }


    fun onDeleteConfirmed(deletedItems: List<ScreenShotItem>) {

        val deletedIds = deletedItems.map { it.id }.toSet()


        _allScreenshots.value =
            _allScreenshots.value.filterNot { it.id in deletedIds }


        deletedIds.forEach { visualHashCache.remove(it) }


        _visualDuplicateGroups.value =
            _visualDuplicateGroups.value.mapNotNull { group ->

                val remaining =
                    (listOf(group.original) + group.duplicates)
                        .filterNot { it.id in deletedIds }

                if (remaining.size < 2) return@mapNotNull null

                DuplicateGroup(
                    original = remaining.first(),
                    duplicates = remaining.drop(1)
                )
            }


        hasVisualScanCompleted = false
    }


    private fun startBackgroundOcr() {
        viewModelScope.launch(Dispatchers.IO) {

            val mutable = _allScreenshots.value.toMutableList()

            mutable.forEachIndexed { index, item ->

                if (item.category != ScreenshotCategory.SCREENSHOTS)
                    return@forEachIndexed

                val text =
                    OcrTextExtractor.extractText(
                        getApplication(),
                        item.uri
                    )

                val newCategory =
                    OcrScreenshotClassifier.classify(text)

                if (newCategory != item.category) {
                    mutable[index] = item.copy(
                        category = newCategory,
                        extractedText = text
                    )
                    _allScreenshots.value = mutable.toList()
                }
            }
        }
    }


    init {
        loadScreenshots()
    }

    private fun loadScreenshots() {
        viewModelScope.launch {
            _allScreenshots.value = repository.getScreenshots()
            startBackgroundOcr()
        }
    }

    fun selectCategory(category: ScreenshotCategory) {
        _selectedCategory.value = category
    }
}
