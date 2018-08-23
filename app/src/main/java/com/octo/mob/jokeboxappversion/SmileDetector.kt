package com.octo.mob.jokeboxappversion

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions

class SmileDetector(private val view: JokeView) {

    private val detector: FirebaseVisionFaceDetector

    init {
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .build()

        detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
    }

    fun recognizePicture(bitmap: Bitmap) {
        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)

        detector.detectInImage(firebaseVisionImage)
            .addOnSuccessListener { faces ->
                try {
                    if (faces.first().smilingProbability > 0.30) {
                        view.displaySmile()
                    } else {
                        view.displaySad()
                    }
                } catch (e: NoSuchElementException) {
                    view.displayFail()
                }
            }
            .addOnFailureListener {
                view.displayFail()
            }
    }
}