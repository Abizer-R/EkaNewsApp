# Eka NewsApp - Offline-First News Reader

## Project Description

The News App is an Android application that provides users with access to the top headlines. The app is designed with an offline-first approach, ensuring that users can view cached articles when there is no internet connection. It fetches data from a news API and caches articles in a local database for offline access. The app also supports saving articles, and users can view saved news separately from cached news. 

Key Features:
- Offline-first functionality: Cached news is available for viewing when offline.
- Display the latest news fetched from an API.
- Save and view saved articles.
- News can be categorized into "Cached" (only available offline) and "Saved" (user-saved articles).

## Steps to Set Up the Project

### 1. Clone the repository
First, clone the repository to your local machine.

``` bash
git clone https://github.com/Abizer-R/EkaNewsApp.git
```

### 2. Open the project in Android Studio
- Open Android Studio and select **Open an Existing Project**.
- Navigate to the folder where you cloned the repository and open it.

### 3. Set up the required API keys (Optional, check note)
To fetch news data from the API, you need to create an account with `News API` and obtain your API key.

- Go to [News API](https://newsapi.org/) and sign up for an API key.
- Add the API key in the `local.properties` file:

``` properties
NEWS_API_KEY=your_api_key
```
#### note: I've added my API key so that project can be run without any extra setup

### 4. Sync Gradle
Once you’ve added the API key, sync the Gradle files with Android Studio.

- Go to **File** > **Sync Project with Gradle Files**.

### 5. Build and run the project
- Select your preferred device (either a physical device or an emulator).
- Click on the **Run** button (Green Play button) in Android Studio to build and run the app.

## Libraries Used

The following libraries have been used in this project:

### 1. **AndroidX Libraries**
- **Core KTX**: Provides Kotlin extensions for Android libraries to make working with them more concise and idiomatic.
- **Navigation Compose**: For navigating between composable screens in a declarative way.
- **Room**: A persistence library that provides an abstraction layer over SQLite, making database access more robust and efficient.
- **Lifecycle KTX**: Helps manage lifecycle-aware components like ViewModel and LiveData.
- **Activity Compose**: Integrates Jetpack Compose with Android's activity lifecycle.

### 2. **Hilt for Dependency Injection**
- **Hilt Android**: A dependency injection library built on top of Dagger to simplify the process of managing dependencies in Android applications.

### 3. **Networking**
- **Retrofit**: A type-safe HTTP client for Android and Java. It is used for fetching data from the News API.
- **Gson Converter**: Converts JSON data into Java/Kotlin objects and vice versa.

### 4. **Coil**
- **Coil Compose**: A lightweight image loading library for Android, used to load images into Composables.

## Purpose of Libraries

- **Core KTX**: Makes Kotlin code more concise and readable.
- **Navigation Compose**: Simplifies navigation between screens using Jetpack Compose.
- **Room**: Handles local data storage and caching, ensuring offline functionality.
- **Hilt**: Manages dependencies for better scalability and easier testing.
- **Retrofit + Gson**: Facilitates network requests and response parsing.
- **Coil**: Efficient image loading for better performance.
