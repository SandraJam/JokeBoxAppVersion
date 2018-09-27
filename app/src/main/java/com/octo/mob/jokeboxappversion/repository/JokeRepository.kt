package com.octo.mob.jokeboxappversion.repository

import com.octo.mob.jokeboxappversion.view.JokeView

interface JokeRepository {
    val view: JokeView

    fun giveMeAJoke()
}