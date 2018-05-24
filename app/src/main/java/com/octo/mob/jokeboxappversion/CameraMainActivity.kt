package com.octo.mob.jokeboxappversion

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_camera.*

class CameraMainActivity : AppCompatActivity(), JokeView {

    private lateinit var smileDetector: SmileDetector
    private lateinit var jokeRepository: JokeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_camera)

        smileDetector = SmileDetector(this)
        jokeRepository = JokeRepository(this)
    }

    override fun displaySmile() {
        jokeTextView.text = getString(R.string.see_you_next_time)
    }

    override fun displaySad() {
        jokeRepository.giveMeAJoke()
    }

    override fun displayFail() {
        textView.text = getString(R.string.fail)
    }

    override fun displayJoke(joke: String) {
        jokeTextView.text = joke
    }
}
