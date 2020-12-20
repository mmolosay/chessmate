package com.ordolabs.chessmate.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.scale
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.util.drawRect
import com.ordolabs.chessmate.util.struct.Chess
import com.ordolabs.chessmate.util.struct.Game

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
            shader = BitmapShader(tilesTemplate, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        }
    }

    private val pieces = arrayOf(
        BitmapFactory.decodeResource(resources, R.drawable.king_white),
        BitmapFactory.decodeResource(resources, R.drawable.king_black),
        BitmapFactory.decodeResource(resources, R.drawable.queen_white),
        BitmapFactory.decodeResource(resources, R.drawable.queen_black),
        BitmapFactory.decodeResource(resources, R.drawable.bishop_white),
        BitmapFactory.decodeResource(resources, R.drawable.bishop_black),
        BitmapFactory.decodeResource(resources, R.drawable.knight_white),
        BitmapFactory.decodeResource(resources, R.drawable.knight_black),
        BitmapFactory.decodeResource(resources, R.drawable.rook_white),
        BitmapFactory.decodeResource(resources, R.drawable.rook_black),
        BitmapFactory.decodeResource(resources, R.drawable.pawn_white),
        BitmapFactory.decodeResource(resources, R.drawable.pawn_black)
    )

    private val game = Game()

    init {
        whiteColor = ResourcesCompat.getColor(resources, R.color.checkboard_white, context.theme)
        blackColor = ResourcesCompat.getColor(resources, R.color.checkboard_black, context.theme)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (width != height) throw IllegalStateException("CheckbardView must have square size")

        makeCheckerboardTemplate()
        scalePieceBitmaps()
    }

    override fun onDraw(c: Canvas?) {
        c ?: return
        drawCheckerboard(c)
        drawPieces(c)
    }

    private fun drawCheckerboard(c: Canvas) {
        c.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawPieces(c: Canvas) {
        game.piecesW.forEach {
            drawPiece(c, it, Chess.Color.WHITE)
        }
        game.piecesB.forEach {
            drawPiece(c, it, Chess.Color.BLACK)
        }
    }

    private fun drawPiece(c: Canvas, piece: Game.Piece, color: Chess.Color) {
        val bitmap = getPieceBitmap(piece.piece, color)
        val left = piece.drawingPosX * tileSize.toFloat()
        val top = piece.drawingPosY * tileSize.toFloat()
        c.drawBitmap(bitmap, left, top, null)
    }

    private fun makeCheckerboardTemplate() {
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

    private fun scalePieceBitmaps() {
        val shouldScale = (pieces[0].getScaledWidth(resources.displayMetrics) > tileSize)
        if (!shouldScale) return

        pieces.forEachIndexed { i, bitmap ->
            pieces[i] = bitmap.scale(tileSize, tileSize)
        }
    }

    private fun getPieceBitmap(piece: Chess.Piece, color: Chess.Color) = when (piece) {
        Chess.Piece.KING -> pieces[0].takeIf { color.isWhite } ?: pieces[1]
        Chess.Piece.QUEEN -> pieces[2].takeIf { color.isWhite } ?: pieces[3]
        Chess.Piece.BISHOP -> pieces[4].takeIf { color.isWhite } ?: pieces[5]
        Chess.Piece.KNIGHT -> pieces[6].takeIf { color.isWhite } ?: pieces[7]
        Chess.Piece.ROOK -> pieces[8].takeIf { color.isWhite } ?: pieces[9]
        Chess.Piece.PAWN -> pieces[10].takeIf { color.isWhite } ?: pieces[11]
    }

    companion object {
        private const val TILE_COUNT = 8
    }
}