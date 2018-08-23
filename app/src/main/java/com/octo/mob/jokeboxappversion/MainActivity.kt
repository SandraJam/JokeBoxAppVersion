package com.octo.mob.jokeboxappversion

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.octo.mob.jokeboxappversion.repository.ApiJokeRepository
import com.octo.mob.jokeboxappversion.repository.JokeRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), JokeView {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val DATA_CHILD = 0
        private const val LOAD_CHILD = 1
    }

    private lateinit var smileDetector: SmileDetector
    private lateinit var jokeRepository: JokeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smileDetector = SmileDetector(this)
        jokeRepository = ApiJokeRepository().apply {
            view = this@MainActivity
        }

        imageView.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
                if (it.resolveActivity(packageManager) != null) {
                    startActivityForResult(it, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            data.extras?.let {
                val imageBitmap = it.get("data") as Bitmap
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.setImageBitmap(imageBitmap)
                mainViewFlipper.displayedChild = LOAD_CHILD
                smileDetector.recognizePicture(imageBitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun displaySmile() {
        textView.text = getString(R.string.see_you_next_time)
        mainViewFlipper.displayedChild = DATA_CHILD
    }

    override fun displaySad() {
        jokeRepository.giveMeAJoke()
    }

    override fun displayFail() {
        textView.text = getString(R.string.fail)
        mainViewFlipper.displayedChild = DATA_CHILD
    }

    override fun displayJoke(joke: String) {
        textView.text = joke
        mainViewFlipper.displayedChild = DATA_CHILD
    }
}
