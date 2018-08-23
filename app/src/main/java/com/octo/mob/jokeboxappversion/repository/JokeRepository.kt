package com.octo.mob.jokeboxappversion.repository

import com.octo.mob.jokeboxappversion.JokeView

interface JokeRepository {
    val view: JokeView

    fun giveMeAJoke()
}