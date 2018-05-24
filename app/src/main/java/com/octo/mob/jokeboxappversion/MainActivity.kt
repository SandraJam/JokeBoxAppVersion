package com.octo.mob.jokeboxappversion

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), JokeView {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private lateinit var smileDetector: SmileDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smileDetector = SmileDetector(this)

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
            smileDetector.recognizePicture(imageBitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun displaySmile() {
        textView.text = "Smile"
    }

    override fun displaySad() {
        textView.text = "Sad"
    }

    override fun displayFail() {
        textView.text = "FAIL"
    }

    override fun displayNoFace() {
        textView.text = "Do you have a face? Oo"
    }
}
