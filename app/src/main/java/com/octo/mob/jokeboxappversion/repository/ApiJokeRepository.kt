package com.octo.mob.jokeboxappversion.repository

import com.octo.mob.jokeboxappversion.view.JokeView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiJokeRepository : JokeRepository {
    override lateinit var view: JokeView
    private val service: DadJokeService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://icanhazdadjoke.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create<DadJokeService>(DadJokeService::class.java)
    }

    override fun giveMeAJoke() {
        launch(CommonPool) {
            val joke = service.dadJoke().execute().body()?.joke ?: ""
            launch(UI) {
                view.displayJoke(joke)
            }
        }
    }
}