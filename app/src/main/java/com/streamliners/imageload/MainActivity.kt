package com.streamliners.imageload

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var chooseBtn: Button
    private lateinit var extractBtn: Button
    private lateinit var timestampInput: EditText
    private lateinit var imageView: ImageView
    private lateinit var videoPathText: TextView

    private var videoPath: String? = null
    private var videoUri: Uri? = null

    private val videoPicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                videoUri = it
                videoPath = getPathFromUri(it)
                videoPathText.text = "Selected: $videoPath"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Reuse the same XML layout

        chooseBtn = findViewById(R.id.btn_choose_video)
        extractBtn = findViewById(R.id.btn_extract_frame)
        timestampInput = findViewById(R.id.et_timestamp)
        imageView = findViewById(R.id.iv_frame)
        videoPathText = findViewById(R.id.tv_video_path)

        chooseBtn.setOnClickListener {
            videoPicker.launch("video/*")
        }

        extractBtn.setOnClickListener {
            val timestamp = timestampInput.text.toString().toDoubleOrNull()
            if (videoPath.isNullOrEmpty() || timestamp == null) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val frame = extractFrameAt(videoPath!!, timestamp)
            if (frame != null) {
                imageView.setImageBitmap(frame)
            } else {
                Toast.makeText(this, "Failed to extract frame", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }

    private fun extractFrameAt(videoPath: String, timestampSec: Double): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoPath)
            val timeUs = (timestampSec * 1_000_000).toLong()
            val frame = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST)
            retriever.release()
            frame
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}