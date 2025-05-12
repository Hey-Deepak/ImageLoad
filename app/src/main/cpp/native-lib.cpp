#include <jni.h>
#include <string>

#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/imgcodecs.hpp>

using namespace cv;

extern "C" {

JNIEXPORT jbyteArray JNICALL
Java_com_streamliners_imageload_ImageProcessor_adjustBrightnessContrast(
        JNIEnv *env, jobject thiz,
        jbyteArray image_data, jfloat brightness, jfloat contrast) {

    // Step 1: Copy jbyteArray to std::vector<uchar>
    jsize length = env->GetArrayLength(image_data);
    std::vector<uchar> buf(length);
    env->GetByteArrayRegion(image_data, 0, length, reinterpret_cast<jbyte*>(buf.data()));

    // Step 2: Decode the image buffer into OpenCV Mat
    cv::Mat img = cv::imdecode(buf, cv::IMREAD_COLOR);  // Auto-detect image type
    if (img.empty()) {
        return nullptr;  // Error decoding
    }

    // Step 3: Adjust brightness & contrast
    cv::Mat output;
    img.convertTo(output, -1, contrast, brightness);  // new = img*contrast + brightness

    // Step 4: Encode the processed Mat back to JPEG
    std::vector<uchar> encoded;
    cv::imencode(".jpg", output, encoded);

    // Step 5: Create a new jbyteArray to return
    jbyteArray result = env->NewByteArray(encoded.size());
    env->SetByteArrayRegion(result, 0, encoded.size(), reinterpret_cast<jbyte*>(encoded.data()));

    return result;
}

}

