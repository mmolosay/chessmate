package com.ordolabs.chessmate.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.scale
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.util.drawRect
import com.ordolabs.chessmate.util.struct.Chess
import com.ordolabs.chessmate.util.struct.Game
import com.ordolabs.chessmate.util.struct.GameViewIteractor

class ChessboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val whiteColor: Int
    private val blackColor: Int

    private val tileSize: Int by lazy {
        width / Chess.TILE_COUNT
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

        isClickable = true
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (width != height) throw IllegalStateException("CheckbardView must have square size")

        makeCheckerboardTemplate()
        scalePieceBitmaps()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val handeled = super.onTouchEvent(event)
        when (event?.action ?: return handeled) {
            MotionEvent.ACTION_UP -> {
                performClick()
                onClick(event)
            }
            else -> return handeled
        }
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
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
        game.pieces.forEach {
            drawPiece(c, it)
        }
    }

    private fun drawPiece(c: Canvas, piece: Game.Piece) {
        val bitmap = getPieceBitmap(piece.type, piece.color)
        val left = GameViewIteractor.getDrawTileX(piece) * tileSize.toFloat()
        val top = GameViewIteractor.getDrawTileY(piece) * tileSize.toFloat()
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

    private fun onClick(event: MotionEvent) {
        val tile = GameViewIteractor.getGameTileOnCoords(event.x, event.y, tileSize)
        val shouldInvalidate = GameViewIteractor.inputTile(tile, game)

        if (shouldInvalidate) invalidate()
    }

    private fun getPieceBitmap(pieceType: Chess.PieceType, color: Chess.Color) = when (pieceType) {
        Chess.PieceType.KING -> pieces[0].takeIf { color.isWhite } ?: pieces[1]
        Chess.PieceType.QUEEN -> pieces[2].takeIf { color.isWhite } ?: pieces[3]
        Chess.PieceType.BISHOP -> pieces[4].takeIf { color.isWhite } ?: pieces[5]
        Chess.PieceType.KNIGHT -> pieces[6].takeIf { color.isWhite } ?: pieces[7]
        Chess.PieceType.ROOK -> pieces[8].takeIf { color.isWhite } ?: pieces[9]
        Chess.PieceType.PAWN -> pieces[10].takeIf { color.isWhite } ?: pieces[11]
    }
}