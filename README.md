# 📚 Factum — Personalized Facts Feed

An Android app delivering personalized daily facts across multiple categories. Users can customize their feed to see only the facts they are interested in and receive push notifications for new content. Designed for scalability, offline access, and smooth user experience.

## 🚀 Features
- ✅ Personalized feed based on user preferences  
- ✅ Offline caching for fast access without internet  
- ✅ Push notifications for new facts  
- ✅ Intuitive and responsive UI with Jetpack Compose  
- ✅ Modular architecture for maintainability and scalability  
- ✅ Optimized performance for smooth scrolling and state updates  

## 🧩 Tech Stack
- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Architecture:** MVVM, Clean Architecture  
- **DI:** Hilt  
- **Networking & Backend:** Firebase SDK  
- **Database:** Room  
- **Other:** Navigation Component, Notifications  

## 📸 Screenshots
| Home | Categories | Edit | Settings |
|-----------|-----------|-----------|-----------|
| ![Home](https://github.com/user-attachments/assets/5e5b880a-b67e-4829-9dc1-3902c982005e) | ![Categories](https://github.com/user-attachments/assets/8904af4d-ba1f-4465-8c40-2a7885eb821d) | ![Edit](https://github.com/user-attachments/assets/aa43cd37-2877-4c46-9bf8-f13a5ba86b3c) | ![Settings](https://github.com/user-attachments/assets/3ddf38ab-38cd-470b-a7cd-7dd3f67f7ef3) |

## ⚙️ How It Works
Factum fetches facts from **Firebase**, caches them locally using **Room**, and displays them in a **personalized feed** built with Jetpack Compose. Notifications are handled through **Firebase Cloud Messaging**, and user preferences are stored locally to provide a tailored experience.

## 🧠 Highlights / Learning Outcomes
- Implemented scalable, offline-first architecture with user personalization  
- Optimized UI for smooth performance and instant state restoration  
- Managed full app lifecycle: architecture, networking, local persistence, notifications  

## 📂 Installation
```bash
# Clone the repo
git clone https://github.com/sean-nam-dev/factum.git

# Open in Android Studio and run the app
