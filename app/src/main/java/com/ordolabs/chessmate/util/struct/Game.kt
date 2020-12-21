package com.ordolabs.chessmate.util.struct

import com.ordolabs.chessmate.util.struct.Chess.Color
import com.ordolabs.chessmate.util.struct.Chess.PosX
import com.ordolabs.chessmate.util.struct.Chess.PosY

class Game {

    var turn: Color = Color.WHITE
    val pieces: ArrayList<Piece> = setupPieces()

    var selected: Piece? = null
        private set

    fun select(piece: Piece) {
        selected = piece
    }

    fun unselect() {
        selected = null
    }

    fun hasSelectedPiece(): Boolean {
        return (selected != null)
    }

    fun moveSelected(dest: Tile) {
        selected?.tile = dest
        unselect()
        changeTurn()
    }

    private fun changeTurn() {
        turn = Color.BLACK.takeIf { turn.isWhite } ?: Color.WHITE
    }

    private fun setupPieces(): ArrayList<Piece> = arrayListOf(
        Piece(Chess.PieceType.ROOK, Tile(PosX.xA, PosY.y1), Color.WHITE),
        Piece(Chess.PieceType.ROOK, Tile(PosX.xH, PosY.y1), Color.WHITE),
        Piece(Chess.PieceType.KNIGHT, Tile(PosX.xB, PosY.y1), Color.WHITE),
        Piece(Chess.PieceType.KNIGHT, Tile(PosX.xG, PosY.y1), Color.WHITE),
        Piece(Chess.PieceType.BISHOP, Tile(PosX.xC, PosY.y1), Color.WHITE),
        Piece(Chess.PieceType.BISHOP, Tile(PosX.xF, PosY.y1), Color.WHITE),
        Piece(Chess.PieceType.QUEEN, Tile(PosX.xD, PosY.y1), Color.WHITE),
        Piece(Chess.PieceType.KING, Tile(PosX.xE, PosY.y1), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xA, PosY.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xB, PosY.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xC, PosY.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xD, PosY.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xE, PosY.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xF, PosY.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xG, PosY.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xH, PosY.y2), Color.WHITE),

        Piece(Chess.PieceType.ROOK, Tile(PosX.xA, PosY.y8), Color.BLACK),
        Piece(Chess.PieceType.ROOK, Tile(PosX.xH, PosY.y8), Color.BLACK),
        Piece(Chess.PieceType.KNIGHT, Tile(PosX.xB, PosY.y8), Color.BLACK),
        Piece(Chess.PieceType.KNIGHT, Tile(PosX.xG, PosY.y8), Color.BLACK),
        Piece(Chess.PieceType.BISHOP, Tile(PosX.xC, PosY.y8), Color.BLACK),
        Piece(Chess.PieceType.BISHOP, Tile(PosX.xF, PosY.y8), Color.BLACK),
        Piece(Chess.PieceType.QUEEN, Tile(PosX.xD, PosY.y8), Color.BLACK),
        Piece(Chess.PieceType.KING, Tile(PosX.xE, PosY.y8), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xA, PosY.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xB, PosY.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xC, PosY.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xD, PosY.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xE, PosY.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xF, PosY.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xG, PosY.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(PosX.xH, PosY.y7), Color.BLACK),
    )

    data class Tile(
        var posX: PosX,
        var posY: PosY
    ) {
        fun modBy(byX: Int = 0, byY: Int = 0): Tile {
            if (byX == 0 && byY == 0) return this
            return Tile(this.posX + byX, this.posY + byY)
        }

        override fun toString(): String {
            return "${posX.mark}${posY.mark}"
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Tile) return false
            return (this.posX == other.posX &&
                    this.posY == other.posY)
        }

        override fun hashCode(): Int {
            // TODO: implement
            return super.hashCode()
        }
    }

    data class Piece(
        var type: Chess.PieceType,
        var tile: Tile,
        val color: Color
    ) {
        val isWhite: Boolean = this.color.isWhite
        val isBlack: Boolean = this.color.isBlack

        override fun toString(): String {
            return "Piece(type=${type.name}, tile=$tile, color=${color.name})"
        }
    }
}