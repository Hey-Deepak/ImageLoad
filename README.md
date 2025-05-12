## Description

This is an Android app designed to load and extract frames from video files. It utilizes Jetpack Compose for UI deavelopment, and OpenCV is integrated for frame extraction functionality. The app also leverages Kotlin and various Android libraries for efficient media handling.

## Current Error
### TL:DR :- Gradle Issue (Also, Due to error in Gradle, Not able to write fully code for android native which is not difficult for me, like pic video form galary and save it)

### Error: Backend Internal Error

While compiling the project, the following error occurs:

`e: org.jetbrains.kotlin.backend.common.BackendException: Backend Internal error: Exception during IR lowering File being compiled: D:/Projets/Android/TheStremliners/ImageLoad/app/src/main/java/com/streamliners/imageload/ExtractFrameScreen.kt`

**Cause:**  
The error seems to occur during the IR (Intermediate Representation) lowering phase of Kotlin compilation, specifically related to the Composable functions and state management in the `ExtractFrameScreen.kt` file.

