package com.streamliners.imageload

import android.graphics.Bitmap

class ImageProcessor {
    init {
        System.loadLibrary("imageload") // Load the C++ shared library
    }

    // Declare native methods
    external fun extractFrameAt(videoPath: String, timestampSeconds: Double): Bitmap?
}