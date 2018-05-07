package com.octo.mob.jokeboxappversion

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.contrib.android.TensorFlowInferenceInterface


class MainActivity : AppCompatActivity() {

    private lateinit var interpreter: TensorFlowInferenceInterface

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val MEAN = 128
        private const val STD = 128F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        interpreter = TensorFlowInferenceInterface(assets, "thegraph.pb")

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

        val imgValues = convertBitmap(bitmap)

        val labels = assets.open("thegraph.txt").bufferedReader().readLines()
        val outputs = FloatArray(labels.size)

        interpreter.feed("input", imgValues, 1, 224, 224, 3)

        interpreter.run(arrayOf("final_result"))

        interpreter.fetch("final_result", outputs)

        return labels.mapIndexed { id, label -> "$label: ${outputs[id]}" }.joinToString("\n")
    }

    private fun convertBitmap(bitmap: Bitmap): FloatArray {
        val pixels = IntArray(224 * 224)
        val imgValues = FloatArray(224 * 224 * 3)
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
        newBitmap.getPixels(pixels, 0, newBitmap.width, 0, 0,    newBitmap.width, newBitmap.height);
        pixels.forEachIndexed { index, pixel ->
            imgValues[index * 3 + 0] = ((pixel shr 16 and 0xFF) - MEAN) / STD
            imgValues[index * 3 + 1] = ((pixel shr 8 and 0xFF) - MEAN) / STD
            imgValues[index * 3 + 2] = ((pixel and 0xFF) - MEAN) / STD
        }

        return imgValues
    }
}
