package com.ordolabs.chessmate.util.struct

object GameViewMapper {

    // because a b c .. starts from top-left corner, as andriod's coords system
    fun getDrawPosX(piece: Game.Piece): Int {
        return piece.posX.ordinal
    }

    // because 1 2 3 .. starts from bottom-left corner, so should be reversed
    fun getDrawPosY(piece: Game.Piece): Int {
        return (7 - piece.posY.ordinal)
    }
}