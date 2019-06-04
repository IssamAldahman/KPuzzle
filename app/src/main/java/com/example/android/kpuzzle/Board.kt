/*
 * Copyright (C) 2019 Issam Aldahman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.kpuzzle

import android.graphics.Bitmap
import java.util.Observable
import kotlin.collections.ArrayList
import kotlin.collections.shuffle

class Board(private val image: Bitmap, private val dimensions: Int) : Observable() {

    private var tiles: ArrayList<Tile> = ArrayList();
    /*Getter methods for attributes*/
    fun getTiles(): ArrayList<Tile> {
        return tiles
    }

    fun getImage(): Bitmap {
        return image
    }

    fun getDimensions(): Int {
        return dimensions
    }

    /*Creates the tiles ArrayList*/
    fun createTiles(): ArrayList<Tile> {
        val squareBitmaps = BitmapUtils.bitmapToSquares(image, dimensions)
        var index = 0
        for (i in 0 until dimensions) {
            for (j in 0 until dimensions) {
                lateinit var tile: Tile
                if (i == dimensions - 1 && j == dimensions - 1) {
                    BlankTile.init(i, j, index)
                    tile = BlankTile.getInstance()
                } else {
                    tile = Tile(squareBitmaps[index], i, j, index)
                }
                index++

                tiles.add(tile)
            }
        }

        return tiles
    }

    /*Pseudo randomly shuffles tiles into a solvable puzzle*/
    fun shuffleTiles(): ArrayList<Tile> {
        do {
            tiles.shuffle()
            var index = 0
            for (i in 0 until dimensions) {
                for (j in 0 until dimensions) {
                    tiles[index].setRow(i)
                    tiles[index].setColumn(j)
                    index++
                }
            }
        } while (!isSolvable() || isWon())


        setChanged()
        notifyObservers(tiles)
        return tiles
    }

    /*Moves a tile upon a valid move done by the user*/
    fun moveTile(tile: Tile): Unit {
        val blankTile: BlankTile = BlankTile.getInstance()

        val validMove: Boolean = (blankTile.getRow() == tile.getRow() &&
                Math.abs(blankTile.getColumn() - tile.getColumn()) == 1) || (blankTile.getColumn() == tile.getColumn() &&
                Math.abs(blankTile.getRow() - tile.getRow()) == 1)

        if (validMove) {
            val blankIndex = tiles.indexOf(blankTile)
            val tileIndex = tiles.indexOf(tile)

            tiles[blankIndex] = tile
            tiles[tileIndex] = blankTile
            val tmpRow = tile.getRow()
            val tmpColumn = tile.getColumn()

            tile.setRow(blankTile.getRow())
            tile.setColumn(blankTile.getColumn())

            blankTile.setRow(tmpRow)
            blankTile.setColumn(tmpColumn)

            setChanged()
            notifyObservers(tile)
        }
    }

    /*Checks whether a a puzzle is solvable or not*/
    private fun isSolvable(): Boolean {
        var inversions = 0

        for (i in 0 until tiles.size) {
            if (tiles[i] !is BlankTile) {
                for (j in i + 1 until tiles.size) {
                    if (tiles[j] !is BlankTile && tiles[j].getNumber() > tiles[i].getNumber()) {
                        inversions++
                    }
                }
            }
        }

        if (dimensions % 2 == 0) {
            val blankRowEven = BlankTile.getInstance().getRow() % 2 == 0
            return (blankRowEven && inversions % 2 != 0)
        } else {
            return inversions % 2 == 0
        }
    }

    /*Checks whether puzzle is won or not*/
    fun isWon(): Boolean {
        for (i in 0 until tiles.size - 1) {
            if (tiles[i].getNumber() + 1 != tiles[i + 1].getNumber()) {
                return false
            }
        }

        return true
    }
}