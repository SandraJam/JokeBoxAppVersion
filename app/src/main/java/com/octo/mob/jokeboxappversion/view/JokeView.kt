package com.octo.mob.jokeboxappversion.view

interface JokeView {
    fun displaySmile()
    fun displaySad()
    fun displayFail()
    fun displayJoke(joke: String)
}