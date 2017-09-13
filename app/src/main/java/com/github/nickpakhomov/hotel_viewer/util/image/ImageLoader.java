package com.github.nickpakhomov.hotel_viewer.util.image;

import android.widget.ImageView;

import com.squareup.picasso.Transformation;

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

interface ImageLoader {
    void load(ImageView view, String url);
    
    void loadWithTransformation(ImageView view, String url, Transformation transformation);
}

