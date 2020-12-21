package com.ordolabs.chessmate.util.struct

object Chess {

    const val TILE_COUNT = 8

    /**
     * Color and side of chess piece, tile or game turn.
     */
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

    /**
     * Types of chess pieces.
     */
    enum class PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN;
    }

    /**
     * Defines verical checkerboard axis. Entities correspond to
     * `a b c d e f g h` of board, that placed horizontally.
     */
    @Suppress("EnumEntryName")
    enum class File(val mark: String) {
        xA("a"), xB("b"), xC("c"), xD("d"), xE("e"), xF("f"), xG("g"), xH("h");

        operator fun plus(amount: Int): File {
            return byOrdinal(ordinal + amount)
        }

        operator fun minus(amount: Int): File {
            return byOrdinal(ordinal - amount)
        }

        companion object {
            fun byOrdinal(ordinal: Int): File {
                return values().find { it.ordinal == ordinal } ?: throw IllegalArgumentException()
            }
        }
    }

    /**
     * Defines horizontal checkerboard axis. Entities correspond to
     * `1 2 3 4 5 6 7 8` of board, that placed vertically.
     */
    @Suppress("EnumEntryName")
    enum class Rank(val mark: String) {
        y1("1"), y2("2"), y3("3"), y4("4"), y5("5"), y6("6"), y7("7"), y8("8");

        operator fun plus(amount: Int): Rank {
            return byOrdinal(ordinal + amount)
        }

        operator fun minus(amount: Int): Rank {
            return byOrdinal(ordinal - amount)
        }

        companion object {
            fun byOrdinal(ordinal: Int): Rank {
                return values().find { it.ordinal == ordinal } ?: throw IllegalArgumentException()
            }
        }
    }
}