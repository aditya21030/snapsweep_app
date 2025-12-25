package com.yousful.snapsweepai.logic

import com.yousful.snapsweepai.data.DuplicateGroup
import com.yousful.snapsweepai.data.ScreenShotItem

object VisualDuplicateDetector {


    private const val MAX_DISTANCE = 8


    fun findVisualDuplicatesUsingCache(
        screenshots: List<ScreenShotItem>,
        hashCache: Map<Long, Long>
    ): List<DuplicateGroup> {

        val result = mutableListOf<DuplicateGroup>()
        val visited = mutableSetOf<Long>()


        val valid = screenshots.filter { hashCache.containsKey(it.id) }

        for (i in valid.indices) {

            val base = valid[i]
            if (visited.contains(base.id)) continue

            val baseHash = hashCache[base.id] ?: continue
            val similar = mutableListOf<ScreenShotItem>()

            for (j in i + 1 until valid.size) {

                val candidate = valid[j]
                if (visited.contains(candidate.id)) continue

                val candidateHash = hashCache[candidate.id] ?: continue

                val distance =
                    PerceptualHashUtil.hammingDistance(
                        baseHash,
                        candidateHash
                    )

                if (distance <= MAX_DISTANCE) {
                    similar.add(candidate)
                    visited.add(candidate.id)
                }
            }

            if (similar.isNotEmpty()) {
                visited.add(base.id)

                result.add(
                    DuplicateGroup(
                        original = base,
                        duplicates = similar
                    )
                )
            }
        }

        return result
    }
}
