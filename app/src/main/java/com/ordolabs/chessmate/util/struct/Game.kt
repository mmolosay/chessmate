package com.ordolabs.chessmate.util.struct

import com.ordolabs.chessmate.util.struct.Chess.Color
import com.ordolabs.chessmate.util.struct.Chess.File
import com.ordolabs.chessmate.util.struct.Chess.Rank

class Game {

    /**
     * Current game turn.
     */
    var turn: Color = Color.WHITE

    /**
     * Actual game pieces.
     */
    val pieces: ArrayList<Piece> = setupPieces()

    /**
     * Actual selected piece. You should first [select] piece
     * before any operation with it, because seleced piece is
     * a target one for all actions.
     */
    var selected: Piece? = null
        private set

    /**
     * Sets [piece] as a [selected] one.
     */
    fun select(piece: Piece) {
        selected = piece
    }

    /**
     * Removes [selected] piece.
     */
    fun unselect() {
        selected = null
    }

    /**
     * Finds out, if [selected] piece is set.
     */
    fun hasSelectedPiece(): Boolean {
        return (selected != null)
    }

    /**
     * Moves [selected] piece on the specified [dest] tile.
     * After that game move is considered as ended, so turn changes
     * and selection clears.
     */
    fun moveSelected(dest: Tile) {
        selected?.tile = dest
        unselect()
        changeTurn()
    }

    /**
     * Changes game current [turn].
     */
    private fun changeTurn() {
        turn = Color.BLACK.takeIf { turn.isWhite } ?: Color.WHITE
    }

    private fun setupPieces(): ArrayList<Piece> = arrayListOf(
        Piece(Chess.PieceType.ROOK, Tile(File.xA, Rank.y1), Color.WHITE),
        Piece(Chess.PieceType.ROOK, Tile(File.xH, Rank.y1), Color.WHITE),
        Piece(Chess.PieceType.KNIGHT, Tile(File.xB, Rank.y1), Color.WHITE),
        Piece(Chess.PieceType.KNIGHT, Tile(File.xG, Rank.y1), Color.WHITE),
        Piece(Chess.PieceType.BISHOP, Tile(File.xC, Rank.y1), Color.WHITE),
        Piece(Chess.PieceType.BISHOP, Tile(File.xF, Rank.y1), Color.WHITE),
        Piece(Chess.PieceType.QUEEN, Tile(File.xD, Rank.y1), Color.WHITE),
        Piece(Chess.PieceType.KING, Tile(File.xE, Rank.y1), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(File.xA, Rank.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(File.xB, Rank.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(File.xC, Rank.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(File.xD, Rank.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(File.xE, Rank.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(File.xF, Rank.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(File.xG, Rank.y2), Color.WHITE),
        Piece(Chess.PieceType.PAWN, Tile(File.xH, Rank.y2), Color.WHITE),

        Piece(Chess.PieceType.ROOK, Tile(File.xA, Rank.y8), Color.BLACK),
        Piece(Chess.PieceType.ROOK, Tile(File.xH, Rank.y8), Color.BLACK),
        Piece(Chess.PieceType.KNIGHT, Tile(File.xB, Rank.y8), Color.BLACK),
        Piece(Chess.PieceType.KNIGHT, Tile(File.xG, Rank.y8), Color.BLACK),
        Piece(Chess.PieceType.BISHOP, Tile(File.xC, Rank.y8), Color.BLACK),
        Piece(Chess.PieceType.BISHOP, Tile(File.xF, Rank.y8), Color.BLACK),
        Piece(Chess.PieceType.QUEEN, Tile(File.xD, Rank.y8), Color.BLACK),
        Piece(Chess.PieceType.KING, Tile(File.xE, Rank.y8), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(File.xA, Rank.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(File.xB, Rank.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(File.xC, Rank.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(File.xD, Rank.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(File.xE, Rank.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(File.xF, Rank.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(File.xG, Rank.y7), Color.BLACK),
        Piece(Chess.PieceType.PAWN, Tile(File.xH, Rank.y7), Color.BLACK),
    )

    data class Tile(
        var file: File,
        var rank: Rank
    ) {
        fun modBy(byX: Int = 0, byY: Int = 0): Tile {
            if (byX == 0 && byY == 0) return this
            return Tile(this.file + byX, this.rank + byY)
        }

        override fun toString(): String {
            return "${file.mark}${rank.mark}"
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Tile) return false
            return (this.file == other.file &&
                    this.rank == other.rank)
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