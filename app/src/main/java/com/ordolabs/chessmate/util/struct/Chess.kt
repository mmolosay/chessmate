package com.ordolabs.chessmate.util.struct

object Chess {

    enum class Color {
        WHITE {
            override val isWhite: Boolean = true
            override val isBlack: Boolean = false
        },
        BLACK {
            override val isWhite: Boolean = false
            override val isBlack: Boolean = true
        };

        abstract val isWhite: Boolean
        abstract val isBlack: Boolean
    }

    enum class Piece {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    enum class PosX(val mark: String) {
        xA("a"), xB("b"), xC("c"), xD("d"), xE("e"), xF("f"), xG("g"), xH("h")
    }

    enum class PosY(val mark: String) {
        y1("1"), y2("2"), y3("3"), y4("4"), y5("5"), y6("6"), y7("7"), y8("8")
    }
}