package com.octo.mob.jokeboxappversion.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.octo.mob.jokeboxappversion.repository.ApiJokeRepository
import com.octo.mob.jokeboxappversion.repository.JokeRepository
import kotlinx.android.synthetic.main.activity_main_camera.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import android.graphics.BitmapFactory
import com.google.firebase.FirebaseApp
import com.octo.mob.jokeboxappversion.R
import com.octo.mob.jokeboxappversion.SmileDetector


class CameraMainActivity : AppCompatActivity(), JokeView {

    companion object {
        private const val DATA_CHILD = 0
        private const val LOAD_CHILD = 1
    }

    private lateinit var smileDetector: SmileDetector
    private lateinit var jokeRepository: JokeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_camera)

        smileDetector = SmileDetector(this)
        jokeRepository = ApiJokeRepository().apply {
            view = this@CameraMainActivity
        }

        Timer("schedule", true).scheduleAtFixedRate(2000, 5000) {
            runOnUiThread {
                mainViewFlipper.displayedChild = LOAD_CHILD
                camera.captureImage { _, photo ->
                    val bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.size)
                    smileDetector.recognizePicture(bitmap)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        camera.onResume()
    }

    override fun onPause() {
        camera.onPause()
        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults)
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