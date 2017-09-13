package com.github.nickpakhomov.hotel_viewer.util.image;

import android.content.Context;
import android.widget.ImageView;

import com.github.nickpakhomov.hotel_viewer.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

public class PicassoImageLoader implements ImageLoader {
    
    private final Picasso picasso;
    private static final String IMAGE_HOST_BASE_URL = "https://raw.githubusercontent.com/NSArtem/HotelsProject/master/JSON/";
    private static final int TRANSFORMATION_RADIUS = 5;
    private static final int TRANSFORMATION_MARGIN = 1;
    
    public PicassoImageLoader(Context context) {
        this.picasso = Picasso.with(context);
    }
    
    @Override
    public void load(ImageView view, String url) {
        picasso.load(url).into(view);
    }
    
    @Override
    public void loadWithTransformation(ImageView view, String url, Transformation transformation) {
        picasso
                .load(url)
                .transform(transformation).into(view);
    }
    
    /**
     * Asynchronously loading Hotel image into image placeholder
     *
     * @param imageView ImageView to be loaded with downloaded image.
     */
    public static void load(final Context context, String imageFile, ImageView imageView) {
        Picasso.with(context)
                .load(IMAGE_HOST_BASE_URL + imageFile)
                .placeholder(R.drawable.ic_placeholder)
                .transform(new RoundedCornersTransformation(TRANSFORMATION_RADIUS, TRANSFORMATION_MARGIN))
                .into(imageView);
    }
}
