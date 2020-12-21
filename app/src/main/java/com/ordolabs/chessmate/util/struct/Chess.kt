package com.ordolabs.chessmate.util.struct

object Chess {

    const val TILE_COUNT = 8

    @Suppress("EnumEntryName")
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

    enum class PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN;
    }

    @Suppress("EnumEntryName")
    enum class PosX(val mark: String) {
        xA("a"), xB("b"), xC("c"), xD("d"), xE("e"), xF("f"), xG("g"), xH("h");

        operator fun plus(amount: Int): PosX {
            return byOrdinal(ordinal + amount)
        }

        operator fun minus(amount: Int): PosX {
            return byOrdinal(ordinal - amount)
        }

        companion object {
            fun byOrdinal(ordinal: Int): PosX {
                return values().find { it.ordinal == ordinal } ?: throw IllegalArgumentException()
            }
        }
    }

    @Suppress("EnumEntryName")
    enum class PosY(val mark: String) {
        y1("1"), y2("2"), y3("3"), y4("4"), y5("5"), y6("6"), y7("7"), y8("8");

        operator fun plus(amount: Int): PosY {
            return byOrdinal(ordinal + amount)
        }

        operator fun minus(amount: Int): PosY {
            return byOrdinal(ordinal - amount)
        }

        companion object {
            fun byOrdinal(ordinal: Int): PosY {
                return values().find { it.ordinal == ordinal } ?: throw IllegalArgumentException()
            }
        }
    }
}