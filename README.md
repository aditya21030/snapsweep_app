# 🧹 SnapSweep — Smart Screenshot Cleaner for Android

SnapSweep is a **native Android application** that helps users clean up **duplicate and visually similar screenshots** safely and efficiently.  
It is designed with a **smart, offline-first approach**, focusing on speed, privacy, and user control.

---

## 📱 App Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/f8592bb6-2daf-4838-b63d-ac8eacd65ff4" width="180"/>
  <img src="https://github.com/user-attachments/assets/1b039166-6cf3-4d93-a7f3-c67da54b49d1" width="180"/>
  <img src="https://github.com/user-attachments/assets/2eeade0d-7d32-478e-b5ac-234521594990" width="180"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/f9345ba2-e129-42c2-88cd-a13ae386b252" width="180"/>
  <img src="https://github.com/user-attachments/assets/91a42082-1c93-46df-b284-1bd2a78cf410" width="180"/>
  <img src="https://github.com/user-attachments/assets/7b1ee493-a3f4-4252-a6f2-08daa3695e17" width="180"/>
</p>

---

## 🚀 Features

- 🔍 **Smart Duplicate Detection**  
  Detects visually similar screenshots using **perceptual image hashing (pHash)** instead of relying only on file names or sizes.

- ⚡ **Fast & Optimized Scanning**  
  Uses **hash caching** to dramatically reduce repeated scan times on large screenshot collections.

- 🧠 **Smart Delete (Safe by Design)**  
  Deletes **only redundant screenshots**, always keeping one original to avoid accidental data loss.

- 🔐 **Scoped Storage–Safe Deletion**  
  Fully compliant with **Android 11+ MediaStore & Scoped Storage**, using system delete permission dialogs.

- 🗂 **Automatic Screenshot Categorization**  
  Uses **on-device OCR (ML Kit)** to classify screenshots such as chats, payments, and app screens.

- 🎨 **Modern Android UI**  
  Built with **Jetpack Compose**, featuring smooth animations, loading states, and responsive layouts.

---

## 🛠 Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Architecture:** MVVM  
- **Async & State:** Coroutines, StateFlow  
- **Image Processing:** Perceptual Hashing (pHash)  
- **OCR:** ML Kit (on-device)  
- **Storage APIs:** MediaStore, Scoped Storage  

---

## 🧠 How It Works

1. Loads screenshots from the device using `MediaStore`
2. Generates perceptual hashes for each screenshot
3. Compares hashes using **Hamming distance**
4. Groups visually similar screenshots
5. Allows user to safely delete duplicates with system permission

All processing happens **locally on-device** — no cloud, no tracking.

---

## 🔒 Privacy First

- ✅ No cloud uploads  
- ✅ No internet dependency  
- ✅ No user data collection  
- ✅ Fully offline processing  

---

## 📌 Use Cases

- Heavy screenshot users (chats, payments, app screens)
- Users running low on storage
- Anyone wanting **safe cleanup without losing important data**

---

## 📈 Performance

- First scan optimized using background processing
- Subsequent scans are significantly faster using cached hashes
- Designed to handle **hundreds or thousands of screenshots**

---

## 🧑‍💻 Developer

**Aditya Jain**  
Android Developer | Kotlin | Jetpack Compose  

---

## 📄 License

This project is for learning and personal use.  
(Add license details if you plan to open source.)

---

### ⭐ If you like this project, consider starring the repo!
