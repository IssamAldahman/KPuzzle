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

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val RESULT_LOAD_IMG = 100
        const val IMAGE_URI_EXTRA = "imageUri"
        const val DIFFICULTY_EXTRA = "difficulty"
    }

    var difficulty = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val logoView = findViewById<ImageView>(R.id.logo_view)
        logoView.setImageResource(R.drawable.ic_gaming)

        val playButton = findViewById<Button>(R.id.play_btn)
        playButton.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
        }

        val difficultyButton = findViewById<Button>(R.id.difficulty_btn)
        displayDifficulty(difficultyButton)
        difficultyButton.setOnClickListener {
            when (difficulty) {
                3 -> difficulty = 4
                4 -> difficulty = 5
                5 -> difficulty = 3
            }
            displayDifficulty(difficultyButton)
        }
    }

    /* updates the UI to display current difficulty
    *  @param difficultyButton: the button the displays difficulty and allows the user to change it*/
    private fun displayDifficulty(difficultyButton: Button) {
        when (difficulty) {
            3 -> difficultyButton.text = getString(R.string.difficulty_easy)
            4 -> difficultyButton.text = getString(R.string.difficulty_medium)
            5 -> difficultyButton.text = getString(R.string.difficulty_hard)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMG && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra(IMAGE_URI_EXTRA, imageUri.toString())
            intent.putExtra(DIFFICULTY_EXTRA, difficulty)
            startActivity(intent)
        }
    }
}
