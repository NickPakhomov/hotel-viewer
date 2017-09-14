package com.github.nickpakhomov.hotel_viewer.util.image

import android.widget.ImageView

import com.squareup.picasso.Transformation

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

internal interface ImageLoader {
    fun load(view: ImageView, url: String)

    fun loadWithTransformation(view: ImageView, url: String, transformation: Transformation)
}

