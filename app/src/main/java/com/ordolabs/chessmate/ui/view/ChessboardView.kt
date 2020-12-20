package com.ordolabs.chessmate.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.util.drawRect

class ChessboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val whiteColor: Int
    private val blackColor: Int

    private val tileSize: Int by lazy {
        width / TILE_COUNT
    }
    private val tilesTemplate: Bitmap by lazy {
        Bitmap.createBitmap(tileSize * 2, tileSize * 2, Bitmap.Config.ARGB_8888)
    }
    private val paint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
//            style = Paint.Style.FILL
            shader = BitmapShader(tilesTemplate, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        }
    }

    init {
        whiteColor = ResourcesCompat.getColor(resources, R.color.checkboard_white, context.theme)
        blackColor = ResourcesCompat.getColor(resources, R.color.checkboard_black, context.theme)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (width != height) throw IllegalStateException("CheckbardView must have square size")

        val tilesCanvas = Canvas(tilesTemplate)
        val tilePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
        tilesCanvas.apply {
            tilePaint.color = whiteColor
            drawRect(0, 0, tileSize, tileSize, tilePaint)
            drawRect(tileSize, tileSize, tileSize * 2, tileSize * 2, tilePaint)
            tilePaint.color = blackColor
            drawRect(0, tileSize, tileSize, tileSize * 2, tilePaint)
            drawRect(tileSize, 0, tileSize * 2, tileSize, tilePaint)
        }
    }

    override fun onDraw(c: Canvas?) {
        c ?: return
        c.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    companion object {
        private const val TILE_COUNT = 8
    }
}