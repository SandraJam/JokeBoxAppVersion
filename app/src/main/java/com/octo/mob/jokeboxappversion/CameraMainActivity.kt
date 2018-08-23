package com.octo.mob.jokeboxappversion

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.octo.mob.jokeboxappversion.repository.FirebaseJokeRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_camera.*

class CameraMainActivity : AppCompatActivity(), JokeView {

    private lateinit var smileDetector: SmileDetector
    private lateinit var firebaseJokeRepository: FirebaseJokeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_camera)

        smileDetector = SmileDetector(this)
        firebaseJokeRepository = FirebaseJokeRepository().apply {
            view = this@CameraMainActivity
        }
    }

    override fun displaySmile() {
        jokeTextView.text = getString(R.string.see_you_next_time)
    }

    override fun displaySad() {
        firebaseJokeRepository.giveMeAJoke()
    }

    override fun displayFail() {
        textView.text = getString(R.string.fail)
    }

    override fun displayJoke(joke: String) {
        jokeTextView.text = joke
    }
}
