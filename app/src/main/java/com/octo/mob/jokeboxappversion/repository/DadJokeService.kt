package com.octo.mob.jokeboxappversion.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface DadJokeService {
    @GET(".")
    @Headers("Accept: application/json")
    fun dadJoke(): Call<Joke>
}

data class Joke (
        val id: String,
        val joke: String,
        val status: String
)