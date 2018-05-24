package com.octo.mob.jokeboxappversion

import com.google.firebase.firestore.FirebaseFirestore

class JokeRepository(private val view: JokeView) {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun giveMeAJoke() {
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