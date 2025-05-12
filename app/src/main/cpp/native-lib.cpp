#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/bitmap.h>
#include <android/log.h>

#define TAG "NativeFrameExtractor"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

using namespace cv;

extern "C"
JNIEXPORT jobject JNICALL
Java_com_streamliners_imageload_ImageProcessor_extractFrameAt(
        JNIEnv *env,
        jobject /* this */,
        jstring videoPath_,
        jdouble timestampSeconds
) {
    const char *videoPath = env->GetStringUTFChars(videoPath_, nullptr);

    VideoCapture cap(videoPath);
    if (!cap.isOpened()) {
        LOGI("Failed to open video");
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        return nullptr;
    }

    cap.set(CAP_PROP_POS_MSEC, timestampSeconds * 1000);  // Go to timestamp

    Mat frame;
    cap >> frame;
    cap.release();
    env->ReleaseStringUTFChars(videoPath_, videoPath);

    if (frame.empty()) {
        LOGI("Empty frame at timestamp %.2f", timestampSeconds);
        return nullptr;
    }

    // Convert to RGBA
    cvtColor(frame, frame, COLOR_BGR2RGBA);

    // Prepare Bitmap
    jclass bitmapCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmap = env->GetStaticMethodID(
            bitmapCls, "createBitmap",
            "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");

    jstring configName = env->NewStringUTF("ARGB_8888");
    jclass bitmapConfigCls = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID valueOf = env->GetStaticMethodID(bitmapConfigCls, "valueOf",
                                               "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");
    jobject bitmapConfig = env->CallStaticObjectMethod(bitmapConfigCls, valueOf, configName);

    jobject bitmap = env->CallStaticObjectMethod(bitmapCls, createBitmap,
                                                 frame.cols, frame.rows, bitmapConfig);

    // Lock pixels
    void *pixels;
    AndroidBitmap_lockPixels(env, bitmap, &pixels);
    memcpy(pixels, frame.data, frame.total() * frame.elemSize());
    AndroidBitmap_unlockPixels(env, bitmap);

    return bitmap;
}
