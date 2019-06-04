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

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.content_play.*
import java.util.*

class PlayActivity : AppCompatActivity(), Observer {

    companion object {
        const val PUZZLE_IMAGE_EXTRA = "puzzleImage"
        const val MOVES_INTENT_EXTRA = "moves"
        const val TIME_INTENT_EXTRA = "time"
    }

    private lateinit var boardView: TableLayout
    private lateinit var board: Board
    private lateinit var scoreMovesView: TextView
    private lateinit var chronometer: Chronometer
    private lateinit var imageUri: Uri
    private var moves: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        /*Preview Puzzle*/
        imageUri = Uri.parse(intent.getStringExtra(MainActivity.IMAGE_URI_EXTRA))
        val puzzleImage: Bitmap = getPuzzleImage(imageUri)
        puzzle_view.setImageBitmap(puzzleImage)
        val scoreView = findViewById<LinearLayout>(R.id.score_view)
        scoreMovesView = findViewById(R.id.score_moves)
        chronometer = findViewById(R.id.chronometer)

        /*Initiate Board*/
        boardView = findViewById(R.id.board_view)
        val dimension: Int = intent.getIntExtra(MainActivity.DIFFICULTY_EXTRA, 3)
        board = Board(puzzleImage, dimension)
        board.addObserver(this)
        val tiles = board.createTiles()

        var index = 0
        for (i in 0 until board.getDimensions()) {
            val tableRow = TableRow(this)
            for (j in 0 until board.getDimensions()) {
                val tileView: ImageView = ImageView(this)
                bindTile(tileView, tiles[index])
                tileView.setOnClickListener {
                    val tile: Tile = it.tag as Tile
                    board.moveTile(tile)
                }
                tableRow.addView(tileView)
                index++
            }

            boardView.addView(tableRow)
        }

        /*Start CountDown*/
        object : CountDownTimer(4000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                counter_view.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                preview_container.visibility = View.GONE
                boardView.visibility = View.VISIBLE
                scoreView.visibility = View.VISIBLE
                board.shuffleTiles()
            }

        }.start()


    }

    /*gets a puzzle image from uri and prepares it for preview*/
    private fun getPuzzleImage(imageUri: Uri): Bitmap {
        val bitmap: Bitmap = BitmapUtils.decodeBitmap(this, imageUri)
        val croppedBitmap: Bitmap = BitmapUtils.centerCropBitmap(bitmap)

        val dimension: Int = resources.displayMetrics.widthPixels

        return BitmapUtils.scaleBitmap(croppedBitmap, dimension, dimension.toInt())
    }

    private fun bindTile(tileView: ImageView, tile: Tile) {
        tileView.tag = tile
        tileView.setImageBitmap(tile.getImage())
    }

    override fun update(observable: Observable, args: Any?) {

        if (args is ArrayList<*>) {/*tiles have been shuffled*/
            val tiles: ArrayList<Tile> = args as ArrayList<Tile>
            val rowsCount = boardView.childCount
            var index = 0
            for (i in 0 until rowsCount) {
                val rowView: TableRow = boardView.getChildAt(i) as TableRow
                val columnsCount = rowView.childCount
                for (j in 0 until columnsCount) {
                    val tileView: ImageView = rowView.getChildAt(j) as ImageView
                    bindTile(tileView, tiles[index])
                    index++
                }
            }
            moves = 0
            scoreMovesView.text = "Moves: $moves"

            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
        } else if (args is Tile) {/*Tile has been moved */
            val tile: Tile = args
            val tileView = boardView.findViewWithTag<ImageView>(tile)

            val blankTile = BlankTile.getInstance()
            val blankView = boardView.findViewWithTag<ImageView>(blankTile)

            bindTile(tileView, blankTile)
            bindTile(blankView, tile)

            moves++
            scoreMovesView.text = "Moves: $moves"
        }

        if (board.isWon()) {
            chronometer.stop()
            val intent = Intent(this, WinningActivity::class.java)
            intent.putExtra(PUZZLE_IMAGE_EXTRA, imageUri.toString())
            intent.putExtra(MOVES_INTENT_EXTRA, moves)
            intent.putExtra(TIME_INTENT_EXTRA, chronometer.text)
            finish()
            startActivity(intent)
        }
    }
}
