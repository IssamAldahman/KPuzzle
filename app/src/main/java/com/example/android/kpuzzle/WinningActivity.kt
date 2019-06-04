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
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_winning.*
import kotlinx.android.synthetic.main.content_winning.*


class WinningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winning)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageUri = Uri.parse(intent.getStringExtra(PlayActivity.PUZZLE_IMAGE_EXTRA))
        val moves = intent.getIntExtra(PlayActivity.MOVES_INTENT_EXTRA, 0)
        val time = intent.getStringExtra(PlayActivity.TIME_INTENT_EXTRA)

        val bitmap: Bitmap = BitmapUtils.decodeBitmap(this, imageUri)
        val croppedBitmap: Bitmap = BitmapUtils.centerCropBitmap(bitmap)

        val wonPuzzleView = findViewById<ImageView>(R.id.winning_puzzle_view)
        wonPuzzleView.setImageBitmap(croppedBitmap)
        Glide.with(this).load(croppedBitmap).circleCrop().into(wonPuzzleView)

        val movesView = findViewById<TextView>(R.id.moves_view)
        movesView.text = "$moves"

        val timeView = findViewById<TextView>(R.id.time_view)
        timeView.text = time

        menuFab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }

        shareFab.setOnClickListener {
            val shareIntent = ShareCompat.IntentBuilder.from(this@WinningActivity)
                .setType("text/plain")
                .setText("Solved a puzzle in the K Puzzle app with $moves steps and it took me $time")
                .intent
            startActivity(shareIntent)
        }
    }
}
