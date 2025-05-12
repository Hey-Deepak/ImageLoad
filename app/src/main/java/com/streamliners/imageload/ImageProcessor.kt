package com.streamliners.imageload

class ImageProcessor {
    init {
        System.loadLibrary("imageload") // Load the C++ shared library
    }

    // Declare native methods
    external fun adjustBrightnessContrast(imageData: ByteArray, brightness: Float, contrast: Float): ByteArray
}