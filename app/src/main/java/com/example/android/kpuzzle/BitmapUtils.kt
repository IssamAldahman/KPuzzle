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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import java.io.InputStream


class BitmapUtils {
      companion object {
        fun decodeBitmap(context: Context, imageUri: Uri): Bitmap {
            val imageStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            return BitmapFactory.decodeStream(imageStream)
        }
        fun scaleBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
            return Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
        fun centerCropBitmap(bitmap: Bitmap): Bitmap {
            val width: Int = bitmap.width
            val height: Int = bitmap.height

            val dimension: Int = Math.min(width, height)

            return ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
        }
        fun bitmapToSquares(bitmap: Bitmap, count: Int): ArrayList<Bitmap> {
            val dimension = bitmap.width / count
            val bitmapSquares = arrayListOf<Bitmap>()
            for (i in 0 until count) {
                for (j in 0 until count) {
                    val square = Bitmap.createBitmap(bitmap, j * dimension, i * dimension, dimension, dimension)
                    bitmapSquares.add(square)
                }
            }

            return bitmapSquares
        }
    }
}