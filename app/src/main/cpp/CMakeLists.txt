cmake_minimum_required(VERSION 3.22.1)

project("imageload")

# Set path to OpenCV Android SDK
set(OpenCV_INCLUDE_DIR "D:/Projets/Android/TheStremliners/ImageLoad/openCV/native/jni/include")
set(OpenCV_LIB_DIR "D:/Projets/Android/TheStremliners/ImageLoad/openCV/native/jni/abi-arm64-v8a")

# Include OpenCV headers
include_directories(${OpenCV_INCLUDE_DIR})

# Add OpenCV shared libs
add_library(opencv_core SHARED IMPORTED)
set_target_properties(opencv_core PROPERTIES IMPORTED_LOCATION ${OpenCV_LIB_DIR}/libopencv_core.so)

add_library(opencv_imgproc SHARED IMPORTED)
set_target_properties(opencv_imgproc PROPERTIES IMPORTED_LOCATION ${OpenCV_LIB_DIR}/libopencv_imgproc.so)

add_library(opencv_imgcodecs SHARED IMPORTED)
set_target_properties(opencv_imgcodecs PROPERTIES IMPORTED_LOCATION ${OpenCV_LIB_DIR}/libopencv_imgcodecs.so)

# Add your native-lib.cpp
add_library(imageload SHARED native-lib.cpp)

# Android native log
find_library(log-lib log)

# Link libraries
target_link_libraries(imageload
        ${log-lib}
        android
        opencv_core
        opencv_imgproc
        opencv_imgcodecs
)
