package com.ordolabs.chessmate.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class TimerConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val resultBitmap by lazy {
        Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    }

    private val resultCanvas by lazy {
        Canvas(resultBitmap)
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.drawBitmap(resultBitmap, 0f, 0f, null)
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        if (indexOfChild(child) == 0) {
            clearResultBitmap()
        }
        return super.drawChild(resultCanvas, child, drawingTime)
    }

    private fun clearResultBitmap() {
        resultBitmap.eraseColor(Color.TRANSPARENT)
    }
}