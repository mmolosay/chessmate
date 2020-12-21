package com.ordolabs.chessmate.util.struct

import com.ordolabs.chessmate.util.struct.Chess.PosX
import com.ordolabs.chessmate.util.struct.Chess.PosY

object GameViewIteractor {

    // because a b c .. starts from top-left corner, as andriod's coords system
    fun getDrawTileX(piece: Game.Piece): Int {
        return piece.tile.posX.ordinal
    }

    // because 1 2 3 .. starts from bottom-left corner, so should be reversed
    fun getDrawTileY(piece: Game.Piece): Int {
        return (7 - piece.tile.posY.ordinal)
    }

    fun getGamePosX(tilex: Int): PosX {
        return PosX.byOrdinal(tilex)
    }

    fun getGamePosY(tiley: Int): PosY {
        return PosY.byOrdinal(7 - tiley)
    }

    fun getGameTileOnCoords(x: Float, y: Float, tileSize: Int): Game.Tile {
        val posx = getGamePosX(x.toInt() / tileSize)
        val posy = getGamePosY(y.toInt() / tileSize)
        return Game.Tile(posx, posy)
    }

    fun getPieceOnTile(tile: Game.Tile, game: Game): Game.Piece? {
        return game.pieces.find { isPieceOnGameTile(it, tile) }
    }

    fun isPieceOfTurn(piece: Game.Piece, game: Game): Boolean {
        return (piece.color == game.turn)
    }

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

    fun tryMoveSelectedPiece(dest: Game.Tile, game: Game): Boolean {
        val piece = game.selected ?: return false
        val available = getPieceAvailableTiles(piece, game)
        if (available.contains(dest)) {
            game.moveSelected(dest)
        }
        return true
    }

    fun getPieceAvailableTiles(piece: Game.Piece, game: Game) = when (piece.type) {
        Chess.PieceType.KING -> TODO()
        Chess.PieceType.QUEEN -> TODO()
        Chess.PieceType.BISHOP -> TODO()
        Chess.PieceType.KNIGHT -> TODO()
        Chess.PieceType.ROOK -> TODO()
        Chess.PieceType.PAWN -> getPawnAvailableTiles(piece, game)
    }

    fun getPawnAvailableTiles(pawn: Game.Piece, game: Game): List<Game.Tile> {
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

    private fun isPawnOnStartPosition(pawn: Game.Piece): Boolean {
        return (pawn.tile.posY == PosY.y2 && pawn.isWhite ||
                pawn.tile.posY == PosY.y7 && pawn.isBlack)
    }

    private fun isPieceOnGameTile(piece: Game.Piece, tile: Game.Tile): Boolean {
        return (piece.tile == tile)
    }
}