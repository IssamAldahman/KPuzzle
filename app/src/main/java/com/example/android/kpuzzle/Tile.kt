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

open class Tile (private val image: Bitmap?, private var row:Int, private var column:Int, private val number:Int){
    fun getImage():Bitmap? {
        return image
    }

    fun getRow():Int {
        return row
    }

    fun getColumn():Int {
        return column
    }

    fun getNumber():Int {
        return number
    }

    fun setColumn(column:Int){
        this.column = column
    }

    fun setRow(row:Int){
        this.row = row
    }
}