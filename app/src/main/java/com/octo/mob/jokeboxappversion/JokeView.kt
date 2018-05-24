package com.octo.mob.jokeboxappversion

interface JokeView {
    fun displaySmile()
    fun displaySad()
    fun displayFail()
    fun displayJoke(joke: String)
}