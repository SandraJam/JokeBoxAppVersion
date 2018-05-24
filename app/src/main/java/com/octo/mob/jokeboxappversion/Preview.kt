package com.octo.mob.jokeboxappversion

import android.app.Activity
import android.graphics.Camera
import android.view.SurfaceHolder
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main_camera.*

class Preview(private val activity: Activity):ViewGroup(activity), SurfaceHolder.Callback {

    init {
       activity.cameraSurfaceView.holder.addCallback(this)
    }

    fun setCamera(camera: Camera) {

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {

    }

}