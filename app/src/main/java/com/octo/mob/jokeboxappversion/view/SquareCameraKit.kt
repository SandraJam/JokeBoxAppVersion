package com.octo.mob.jokeboxappversion.view

import android.content.Context
import android.util.AttributeSet
import com.camerakit.CameraKitView

class SquareCameraKit(context: Context, attrs: AttributeSet) : CameraKitView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}
