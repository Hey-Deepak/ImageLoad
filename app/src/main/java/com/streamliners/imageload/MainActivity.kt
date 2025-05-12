package com.streamliners.imageload

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import androidx.activity.compose.setContent
import com.streamliners.imageload.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExtractFrameScreen()
        }

        startActivityForResult(pickImageIntent, 111)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            val inputStream = contentResolver.openInputStream(selectedImageUri!!)
            val imageBytes = inputStream?.readBytes()
        }
    }


    companion object {
        init {
            System.loadLibrary("imageload")
        }
    }
}