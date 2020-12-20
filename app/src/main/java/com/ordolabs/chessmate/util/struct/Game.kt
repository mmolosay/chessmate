package com.ordolabs.chessmate.util.struct

import com.ordolabs.chessmate.util.struct.Chess.Color
import com.ordolabs.chessmate.util.struct.Chess.PosX
import com.ordolabs.chessmate.util.struct.Chess.PosY

class Game {

    val piecesW: ArrayList<Piece> = setupPieces(Color.WHITE)
    val piecesB: ArrayList<Piece> = setupPieces(Color.BLACK)

    private fun setupPieces(color: Color): ArrayList<Piece> = arrayListOf(
        Piece(Chess.Piece.ROOK, PosX.xA, PosY.y1.takeIf { color.isWhite } ?: PosY.y8),
        Piece(Chess.Piece.ROOK, PosX.xH, PosY.y1.takeIf { color.isWhite } ?: PosY.y8),
        Piece(Chess.Piece.KNIGHT, PosX.xB, PosY.y1.takeIf { color.isWhite } ?: PosY.y8),
        Piece(Chess.Piece.KNIGHT, PosX.xG, PosY.y1.takeIf { color.isWhite } ?: PosY.y8),
        Piece(Chess.Piece.BISHOP, PosX.xC, PosY.y1.takeIf { color.isWhite } ?: PosY.y8),
        Piece(Chess.Piece.BISHOP, PosX.xF, PosY.y1.takeIf { color.isWhite } ?: PosY.y8),
        Piece(Chess.Piece.QUEEN, PosX.xD, PosY.y1.takeIf { color.isWhite } ?: PosY.y8),
        Piece(Chess.Piece.KING, PosX.xE, PosY.y1.takeIf { color.isWhite } ?: PosY.y8),
        Piece(Chess.Piece.PAWN, PosX.xA, PosY.y2.takeIf { color.isWhite } ?: PosY.y7),
        Piece(Chess.Piece.PAWN, PosX.xB, PosY.y2.takeIf { color.isWhite } ?: PosY.y7),
        Piece(Chess.Piece.PAWN, PosX.xC, PosY.y2.takeIf { color.isWhite } ?: PosY.y7),
        Piece(Chess.Piece.PAWN, PosX.xD, PosY.y2.takeIf { color.isWhite } ?: PosY.y7),
        Piece(Chess.Piece.PAWN, PosX.xE, PosY.y2.takeIf { color.isWhite } ?: PosY.y7),
        Piece(Chess.Piece.PAWN, PosX.xF, PosY.y2.takeIf { color.isWhite } ?: PosY.y7),
        Piece(Chess.Piece.PAWN, PosX.xG, PosY.y2.takeIf { color.isWhite } ?: PosY.y7),
        Piece(Chess.Piece.PAWN, PosX.xH, PosY.y2.takeIf { color.isWhite } ?: PosY.y7),
    )

    data class Piece(
        var piece: Chess.Piece,
        var posX: PosX,
        var posY: PosY
    ) {
        // because a b c .. starts from top-left corner, as andriod's coords system
        val drawingPosX: Int
            get() = (posX.ordinal)
        // because 1 2 3 .. starts from bottom-left corner, so should be reversed
        val drawingPosY: Int
            get() = (7 - posY.ordinal)
    }
}