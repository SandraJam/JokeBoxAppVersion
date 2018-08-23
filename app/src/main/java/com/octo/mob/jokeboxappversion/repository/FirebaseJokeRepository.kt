package com.octo.mob.jokeboxappversion.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.octo.mob.jokeboxappversion.JokeView

class FirebaseJokeRepository: JokeRepository {

    override lateinit var view: JokeView

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun giveMeAJoke() {
        database.collection("jokes")
                .get()
                .addOnSuccessListener {
                    val joke = it.documents.shuffled().first().data?.get("joke") as String
                    view.displayJoke(joke)
                }
                .addOnFailureListener {
                    view.displayFail()
                }
    }
}