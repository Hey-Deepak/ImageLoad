package com.streamliners.imageload

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExtractFrameScreen() {
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var videoPath by remember { mutableStateOf("") }
    var timestampText by remember { mutableStateOf("") }
    var frameBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        videoUri = it
        videoPath = it?.let { uri ->
            getPathFromUri(context, uri)
        }.orEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = { launcher.launch("video/*") }) {
            Text("Choose Video")
        }

        if (videoUri != null) {
            Text("Selected: $videoPath", fontSize = 12.sp, maxLines = 2)
        }

        OutlinedTextField(
            value = timestampText,
            onValueChange = { timestampText = it },
            label = { Text("Timestamp (seconds)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            val timestamp = timestampText.toDoubleOrNull()
            if (videoPath.isNotBlank() && timestamp != null) {
                frameBitmap = ImageProcessor().extractFrameAt(videoPath, timestamp)
            }
        }) {
            Text("Extract Frame")
        }

        frameBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(bitmap.width / bitmap.height.toFloat())
            )
        }
    }
}


fun getPathFromUri(context: Context, uri: Uri): String? {
    val projection = arrayOf(MediaStore.Video.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        if (it.moveToFirst()) {
            return it.getString(columnIndex)
        }
    }
    return null
}

