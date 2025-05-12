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

    private lateinit var binding: ActivityMainBinding
    val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ImageProcessingScreen()
        }


        startActivityForResult(pickImageIntent, 111)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            val inputStream = contentResolver.openInputStream(selectedImageUri!!)
            val imageBytes = inputStream?.readBytes()
            // Now you can pass this byte array to the C++ function
        }
    }


    companion object {
        // Used to load the 'imageload' library on application startup.
        init {
            System.loadLibrary("imageload")
        }
    }
}