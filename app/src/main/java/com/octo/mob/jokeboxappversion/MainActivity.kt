package com.octo.mob.jokeboxappversion

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel




class MainActivity : AppCompatActivity() {

    private lateinit var tflite: Interpreter

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val modelFile = assets.openFd("opti_thegraph.lite").let {
            FileInputStream(it.fileDescriptor).channel.map(
                FileChannel.MapMode.READ_ONLY,
                it.startOffset,
                it.declaredLength
            )
        }

        tflite = Interpreter(modelFile)

        button.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
                if (it.resolveActivity(packageManager) != null) {
                    startActivityForResult(it, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data.extras
            val imageBitmap = extras.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            textView.text = recognizePicture(imageBitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun recognizePicture(bitmap: Bitmap): String {
        val pixels = IntArray(224 * 224)
        val imgData = ByteBuffer.allocateDirect(1 * 224 * 224 * 3).apply {
            order(ByteOrder.nativeOrder())
        }
        val labels = assets.open("thegraph.txt").bufferedReader().readLines()
        val outputs = Array(1) { ByteArray(labels.size) }

        val newBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
        newBitmap.getPixels(pixels, 0, newBitmap.width, 0, 0, newBitmap.width, newBitmap.height)

        pixels.forEach { pixel ->
            imgData.putFloat((pixel shr 16 and 0xFF).toFloat())
            imgData.putFloat((pixel shr 8 and 0xFF).toFloat())
            imgData.putFloat((pixel and 0xFF).toFloat())
        }

        tflite.run(imgData, outputs)

        return labels.mapIndexed { id, label -> Pair(label, outputs[0][id]) }.joinToString { "\n" }
    }
}
