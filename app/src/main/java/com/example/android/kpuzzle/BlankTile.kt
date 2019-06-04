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

/*Implementing BlankTile as a singleton makes it easy to keep track of the blank tile and ensures
* that there is only one instance of it*/
class BlankTile private constructor(row:Int, column:Int, number:Int) : Tile(null, row, column, number) {
    companion object {

        private var instance: BlankTile? = null

        fun getInstance(): BlankTile {
            if (instance == null){
                throw AssertionError("BlankTile instance must be initialized before calling getInstance()")
            }
            return instance as BlankTile
        }

        fun init(row:Int, column:Int, number:Int){
            instance = BlankTile(row, column, number)
        }
    }
}
