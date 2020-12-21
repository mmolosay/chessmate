package com.ordolabs.chessmate.util.struct

import com.ordolabs.chessmate.util.struct.Chess.File
import com.ordolabs.chessmate.util.struct.Chess.Rank

object GameProcessor {

    /**
     * Returns an ordinal of [piece] tile's X, e.g. for `c` it would be `2`.
     * Because a b c .. start from left corner and goes right, as andriod's X axis.
     */
    fun getDrawFile(piece: Game.Piece): Int {
        return piece.tile.file.ordinal
    }

    /**
     * Returns an ordinal of [piece] tile's Y, e.g. for `3` it would be `4`.
     * Because 1 2 3 .. start from bottom corner and goes up, it should be reversed.
     */
    fun getDrawRank(piece: Game.Piece): Int {
        return (7 - piece.tile.rank.ordinal)
    }

    /**
     * Returns [File] of view tile with [tileX] as X value.
     * It's an opposite to [getDrawTileX].
     */
    fun getGameFile(tileX: Int): File {
        return File.byOrdinal(tileX)
    }

    /**
     * Returns [Rank] of view tile with [tileY] as X value.
     * It's an opposite to [getDrawRank].
     */
    fun getGameRank(tileY: Int): Rank {
        return Rank.byOrdinal(7 - tileY)
    }

    /**
     * Locates [Game.Tile], which corresponds to specified [x] and [y] pixel.
     *
     * @param tileSize an integer size of view's tile.
     */
    fun getGameTileOnCoords(x: Float, y: Float, tileSize: Int): Game.Tile {
        val posx = getGameFile(x.toInt() / tileSize)
        val posy = getGameRank(y.toInt() / tileSize)
        return Game.Tile(posx, posy)
    }

    /**
     * Seeks for a piece on [tile].
     *
     * @return piece on specified tile; `null` otherwise.
     */
    fun getPieceOnTile(tile: Game.Tile, game: Game): Game.Piece? {
        return game.pieces.find { isPieceOnTile(it, tile) }
    }

    /**
     * Finds out, if specified [piece] belongs to current turn's side.
     */
    fun isPieceOfTurn(piece: Game.Piece, game: Game): Boolean {
        return (piece.color == game.turn)
    }

    /**
     * Processes [tile] of [game].
     *
     * @return `true` if pieces' data was changed; `false` otherwise.
     */
    fun inputTile(tile: Game.Tile, game: Game): Boolean {
        val piece = getPieceOnTile(tile, game)

        if (game.hasSelectedPiece()) {
            if (piece != null) {
                // reselect piece, if same color
                if (isPieceOfTurn(piece, game)) {
                    game.select(piece)
                    return false
                }
                // else, take opponent's piece
                else {
                    TODO()
                }
            }
            // move piece
            else {
                return tryMoveSelectedPiece(tile, game)
            }
        } else {
            if (piece != null) {
                // select piece
                if (isPieceOfTurn(piece, game)) {
                    game.select(piece)
                    return false
                }
                // ignore opponent's piece
                else {
                    return false
                }
            }
            // empty tile
            else {
                return false
            }
        }
    }

    /**
     * Collects all tiles, available for [game]'s selected piece to move
     * and moves it to [dest], if that tile is one of the available.
     *
     * @return `true` if pieces' data was changed; `false` otherwise.
     */
    private fun tryMoveSelectedPiece(dest: Game.Tile, game: Game): Boolean {
        val piece = game.selected ?: return false
        val available = getPieceAvailableTiles(piece, game)
        if (available.contains(dest)) {
            game.moveSelected(dest)
        }
        return true
    }

    /**
     * Collects all tiles, avaliable for [piece] to take, no matter
     * the piece would just move on it or take opponent's piece too.
     */
    private fun getPieceAvailableTiles(piece: Game.Piece, game: Game) = when (piece.type) {
        Chess.PieceType.KING -> TODO()
        Chess.PieceType.QUEEN -> TODO()
        Chess.PieceType.BISHOP -> TODO()
        Chess.PieceType.KNIGHT -> TODO()
        Chess.PieceType.ROOK -> TODO()
        Chess.PieceType.PAWN -> getPawnAvailableTiles(piece, game)
    }

    private fun getPawnAvailableTiles(pawn: Game.Piece, game: Game): List<Game.Tile> {
        val tiles = mutableListOf<Game.Tile>()
        var nextTile = pawn.tile.modBy(byY = 1.takeIf { pawn.isWhite } ?: -1)
        var nextTilePiece = getPieceOnTile(nextTile, game)
        if (nextTilePiece != null) {
            return tiles
        } else {
            tiles.add(nextTile)
        }
        if (isPawnOnStartPosition(pawn)) {
            nextTile = pawn.tile.modBy(byY = 2.takeIf { pawn.isWhite } ?: -2)
            nextTilePiece = getPieceOnTile(nextTile, game)
            if (nextTilePiece != null) {
                return tiles
            } else {
                tiles.add(nextTile)
            }
        }
        return tiles
    }

    /**
     * Returns `true`, if pawn is on its start position —
     *
     */
    private fun isPawnOnStartPosition(pawn: Game.Piece): Boolean {
        return (pawn.tile.rank == Rank.y2 && pawn.isWhite ||
                pawn.tile.rank == Rank.y7 && pawn.isBlack)
    }

    /**
     * Finds out, if specified [piece] is on checkerboard's [tile].
     */
    private fun isPieceOnTile(piece: Game.Piece, tile: Game.Tile): Boolean {
        return (piece.tile == tile)
    }
}