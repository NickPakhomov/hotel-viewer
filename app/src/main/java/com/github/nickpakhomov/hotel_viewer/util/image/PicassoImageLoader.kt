package com.github.nickpakhomov.hotel_viewer.util.image

import android.content.Context
import android.widget.ImageView

import com.github.nickpakhomov.hotel_viewer.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

class PicassoImageLoader(context: Context) : ImageLoader {

    private val picasso: Picasso = Picasso.with(context)

    override fun load(view: ImageView, url: String) {
        picasso.load(url).into(view)
    }

    override fun loadWithTransformation(view: ImageView, url: String, transformation: Transformation) {
        picasso
                .load(url)
                .transform(transformation).into(view)
    }

    companion object {
        private val IMAGE_HOST_BASE_URL = "https://raw.githubusercontent.com/NSArtem/HotelsProject/master/JSON/"
        private val TRANSFORMATION_RADIUS = 5
        private val TRANSFORMATION_MARGIN = 1

        /**
         * Asynchronously loading Hotel image into image placeholder
         *
         * @param imageView ImageView to be loaded with downloaded image.
         */
        fun load(context: Context, imageFile: String?, imageView: ImageView) {
            Picasso.with(context)
                    .load(IMAGE_HOST_BASE_URL + imageFile)
                    .placeholder(R.drawable.ic_placeholder)
                    .transform(RoundedCornersTransformation(TRANSFORMATION_RADIUS, TRANSFORMATION_MARGIN))
                    .into(imageView)
        }
    }
}
