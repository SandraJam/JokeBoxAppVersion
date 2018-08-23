package com.octo.mob.jokeboxappversion

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.octo.mob.jokeboxappversion.repository.ApiJokeRepository
import com.octo.mob.jokeboxappversion.repository.JokeRepository
import android.support.v4.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import kotlinx.android.synthetic.main.activity_main.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class MainActivity : AppCompatActivity(), JokeView {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val DATA_CHILD = 0
        private const val LOAD_CHILD = 1
        private const val FILE_PROVIDER_AUTHORITY = "com.octo.mob.jokeboxappversion.fileprovider"
        private const val DATE_FORMAT_PATTERN = "yyyyMMdd_HHmmss"
        private const val JPEG_PREFIX = "JPEG_"
        private const val JPG_EXTENSION = ".jpg"
    }

    private lateinit var smileDetector: SmileDetector
    private lateinit var jokeRepository: JokeRepository
    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smileDetector = SmileDetector(this)
        jokeRepository = ApiJokeRepository().apply {
            view = this@MainActivity
        }

        imageView.setOnClickListener {
            with(Intent(MediaStore.ACTION_IMAGE_CAPTURE)) {
                if (resolveActivity(packageManager) != null) {
                    putExtra(MediaStore.EXTRA_OUTPUT, createPhotoUri())
                    startActivityForResult(this, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            with(getPicture()) {
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.setImageBitmap(this)
                mainViewFlipper.displayedChild = LOAD_CHILD
                smileDetector.recognizePicture(this)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createPhotoUri(): Uri = FileProvider.getUriForFile(
        this,
        FILE_PROVIDER_AUTHORITY,
        createImageFile()
    )

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.FRANCE).format(Date())
        val imageFileName = "$JPEG_PREFIX$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, JPG_EXTENSION, storageDir)
        currentPhotoPath = image.absolutePath
        return image
    }

    private fun getPicture(): Bitmap {
        val bmOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(currentPhotoPath, this)
            inJustDecodeBounds = false
        }
        return BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
    }

    override fun displaySmile() {
        val color = listOf(R.color.colorPrimary, R.color.colorPrimaryDark,
            R.color.colorAccent).map {
            ContextCompat.getColor(applicationContext, it)
        }

        textView.text = getString(R.string.see_you_next_time)
        mainViewFlipper.displayedChild = DATA_CHILD
        konfettiView.build()
            .addColors(color)
            .setDirection(0.0, 359.0)
            .setFadeOutEnabled(true)
            .addShapes(Shape.CIRCLE)
            .setSpeed(1f, 8f)
            .setTimeToLive(4000L)
            .addSizes(Size(12), Size(16, 6f))
            .setPosition(konfettiView.x + konfettiView.width / 2,
                konfettiView.y + konfettiView.height / 3)
            .burst(100)
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
