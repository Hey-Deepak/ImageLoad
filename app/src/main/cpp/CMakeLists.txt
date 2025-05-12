cmake_minimum_required(VERSION 3.22.1)

project("imageload")

# OpenCV setup
set(OpenCV_STATIC ON)
set(OpenCV_DIR "D:/Projets/Android/TheStremliners/ImageLoad/openCV/native/jni")
find_package(OpenCV REQUIRED)

include_directories(${OpenCV_INCLUDE_DIRS})
set(OpenCV_LIB_PATH "D:/Projets/Android/TheStremliners/ImageLoad/openCV/native/jni/abi-arm64-v8a") # 🔁 Replace with actual ABI
link_directories(${OpenCV_LIB_PATH})

add_library(imageload SHARED native-lib.cpp)

find_library(log-lib log)

target_link_libraries(
        imageload
        libcpufeatures
        ${log-lib}
        android
        ${OpenCV_LIBS}
)
