# Search from Share

**Search from Share** is a lightweight Android utility app that allows you to search Google directly from the Android Share menu. It accepts shared text, processes it to extract the search query, and opens the results instantly.

## 🚀 Key Features

*   **Instant Search**: Simply share any text to "Search from Share" to start a Google search immediately.
*   **Native Performance**: Built with pure Android Native (Kotlin) to eliminate engine startup overhead and ensure zero UI lag.
*   **Chrome Custom Tabs**: Search results open within the app using Chrome Custom Tabs for a fast, integrated, and secure browsing experience.
*   **Smart Text Processing**: Automatically cleans shared text by removing URLs and surrounding quotes to find the actual search query.
*   **Unobtrusive**: The app runs as a transparent activity and does not clutter your app drawer (hidden from launcher).
*   **User Feedback**: Provides immediate feedback (Toast) if the shared text is empty or invalid.

## 🛠 Technical Stack

*   **Language**: Kotlin
*   **Architecture**: Native Android (Activity-based)
*   **Browser Integration**: Chrome Custom Tabs (`androidx.browser:browser`)
*   **Build System**: Gradle (Kotlin DSL)
*   **CI/CD**: GitHub Actions

## 📦 Installation

This app is designed to be installed via APK.
1.  Download the latest APK from the [Releases](https://github.com/mystster/search-from-share/releases) page.
2.  Install the APK on your Android device.
3.  Select text in any app, tap "Share", and choose "Search from Share".

## 💻 Development

### Prerequisites
*   Android Studio
*   JDK 17

### Build
To build the debug APK:
```bash
./gradlew assembleDebug
```

To build the release APK (requires signing configuration):
```bash
./gradlew assembleRelease
```
