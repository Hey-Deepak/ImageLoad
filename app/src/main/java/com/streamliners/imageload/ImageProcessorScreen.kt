package com.streamliners.imageload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.nio.ByteBuffer

@Composable
fun ImageProcessingScreen() {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var processedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            val imageBytes = context.contentResolver.openInputStream(it)?.readBytes()
            imageBytes?.let { bytes ->
                // Pass image bytes to C++ function via JNI
                val processedBytes = ImageProcessor().adjustBrightnessContrast(bytes, 30f, 1.1f)

                // Convert processed byte array to Bitmap
                processedBitmap = BitmapFactory.decodeByteArray(processedBytes, 0, processedBytes.size)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Pick Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        processedBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Processed Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}


/*fun processImageWithJNI(imageData: ByteArray, brightness: Float, contrast: Float): Bitmap? {
    val imageProcessor = ImageProcessor()
    val processedData = imageProcessor.adjustBrightnessContrast(imageData, brightness, contrast)

    // Convert the byte array back to Bitmap
    val byteBuffer = ByteBuffer.wrap(processedData)
    return BitmapFactory.decodeByteArray(byteBuffer.array(), 0, byteBuffer.array().size)
}*/
