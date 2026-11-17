# Study Planner

An Android app for students who want to organize study tasks, assignments, and deadlines. This progress version demonstrates one complete feature: create a task, display it in a list, mark it complete, and keep its state after restarting the app.

## Completed feature

- Enter a task title and tap **Add task**.
- See the task in a scrolling list.
- Tick its checkbox to mark it complete. Its title gets a line through it.
- Untick it to mark it unfinished again.
- Close and reopen the app. Task titles and completion states remain saved.
- Blank or spaces-only titles cannot be added.

## How to run in Android Studio

1. Clone this repository, or choose **Code > Download ZIP** on GitHub and extract it.
   ```text
   git clone https://github.com/selimkahi/Study-Planner.git
   ```
2. Open the **Study-Planner** folder in Android Studio. Select the root folder containing `settings.gradle.kts` and `gradlew.bat`.
3. Allow Gradle to sync. Internet access is needed the first time to download dependencies.
4. Use the JDK 25 bundled with the tested Android Studio installation. The project's Gradle daemon configuration requests Java 25.
5. In **Tools > SDK Manager**, install the Android API 37 platform required by `compileSdk` and Android SDK Build-Tools 36.0.0. Accept any requested SDK licenses.
6. Start an emulator, or connect an Android phone with USB debugging enabled. The app supports Android 7.0 / API 24 or newer.
7. Select the **app** run configuration and press **Run**.

Tested locally with Android Studio 2026.1.4 and its bundled JDK 25.0.3. Project versions are Android Gradle Plugin 9.4.1 and Gradle 9.6.0. Use an Android Studio version compatible with those project versions.

Android Studio normally creates `local.properties` with your own SDK location. This machine-specific file is intentionally excluded from Git. If the SDK cannot be found, select its location in Android Studio's SDK settings and sync again.

### Build from the terminal

With the Android SDK configured and `JAVA_HOME` pointing to JDK 25, run from the project root:

Windows PowerShell:
```powershell
.\gradlew.bat :app:assembleDebug
```

macOS / Linux:
```sh
chmod +x gradlew
./gradlew :app:assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## Demonstration

1. Enter **Read chapter 2** and tap **Add task**.
2. Confirm the task appears in the list with an unchecked checkbox.
3. Tick it and confirm the title has a line through it.
4. Close the app, then reopen **Study Planner**. For a stronger check, force-stop it from Android's app settings before reopening.
5. Confirm the task is still present and checked.
6. Untick it, restart again, and confirm it stays unchecked.

Wait for the updated checkbox state to appear before closing the app. Clearing app data or uninstalling the app removes the local saved tasks.

## Brief technical design

- **Screen:** one Jetpack Compose screen, `StudyPlannerScreen`, containing the input, button, and `LazyColumn`. This progress version needs no navigation between screens.
- **Task:** a small Kotlin data class with `title: String` and `isCompleted: Boolean`.
- **ViewModel:** `StudyPlannerViewModel.kt` handles `addTask` and `setCompleted` using `viewModelScope`. It exposes the task list as StateFlow. The temporary text being typed remains local Compose state.
- **Local storage:** `TaskStorage.kt` saves a JSON list in Preferences DataStore on the device.
- **Data flow:** the screen reports an action to the ViewModel, which calls `TaskStorage`. DataStore emits the saved list, the ViewModel shares it as StateFlow, and `collectAsStateWithLifecycle` observes it in Compose. Restarting the app loads the same stored values.
- **Current scope:** the demonstrated feature works locally with a ViewModel. API integration, additional screens, and background reminders are planned work.

Main source files:

```text
app/src/main/java/com/example/studyplanner/
    MainActivity.kt            # Screen and ViewModel setup
    StudyPlannerViewModel.kt   # Task actions and observable screen data
    TaskStorage.kt             # Task model, saving, and loading
```

## Remaining scope and next steps

1. Add course and deadline fields, then task editing and filtering/grouping.
2. Add navigation as additional screens are introduced, and expand the ViewModel's screen state as needed.
3. Add one useful task reminder using notifications and an appropriate scheduling option. WorkManager is only a proposed option; it is not implemented in this version.
4. Select a relevant course-approved API and add loading and error states for the final project.
5. Recheck the complete planner, improve input validation, and prepare the final report and demonstration.

The full final application is still in progress. This submission focuses on the completed task creation, completion, and persistence feature.
