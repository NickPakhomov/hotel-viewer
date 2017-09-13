package com.github.nickpakhomov.hotel_viewer.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.nickpakhomov.hotel_viewer.R;
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract;
import com.github.nickpakhomov.hotel_viewer.provider.HotelProvider;
import com.github.nickpakhomov.hotel_viewer.util.image.PicassoImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.github.nickpakhomov.hotel_viewer.R.id.tv_distance;

/**
 * Implementation of presenter layer of DetailActivity
 * Created by Nikolay Pakhomov on 08/09/17.
 */

class DetailPresenterImpl implements DetailPresenter {
    
    private static final String LOG_TAG = DetailPresenterImpl.class.getSimpleName();
    
    private static final int LOADER_ID_HOTELS = 0;
    
    private static final int GOOGLE_MAPS_ZOOM_LEVEL = 12;
    private static final float MPH_TO_KPH_COEF = 1.60934f;
    
    private static final String[] HOTEL_PROJECTION = {
            HotelContract.COLUMN_HOTEL_ID,
            HotelContract.COLUMN_HOTEL_NAME,
            HotelContract.COLUMN_HOTEL_ADDRESS,
            HotelContract.COLUMN_HOTEL_STARS,
            HotelContract.COLUMN_HOTEL_DISTANCE,
            HotelContract.COLUMN_HOTEL_IMAGE_URL,
            HotelContract.COLUMN_HOTEL_SUITS_AVAILABILITY,
            HotelContract.COLUMN_HOTEL_LATITUDE,
            HotelContract.COLUMN_HOTEL_LONGITUDE,
    };
    
    private static final int COL_NUM_HOTEL_ID = 0;
    private static final int COL_NUM_HOTEL_NAME = 1;
    private static final int COL_NUM_HOTEL_ADDRESS = 2;
    private static final int COL_NUM_HOTEL_STARS = 3;
    private static final int COL_NUM_HOTEL_DISTANCE = 4;
    private static final int COL_NUM_HOTEL_IMAGE_URL = 5;
    private static final int COL_NUM_HOTEL_SUITS_AVAILABILITY = 6;
    private static final int COL_NUM_HOTEL_LATITUDE = 7;
    private static final int COL_NUM_HOTEL_LONGITUDE = 8;
    
    private float mLat;
    private float mLon;
    
    private final TextView tv_hotelName;
    private final TextView tv_hotelAddress;
    private final TextView tv_hotelDistance;
    private final TextView tv_suitsAvailable;
    private final RatingBar rb_hotelStars;
    private final ImageView iv_hotelImage;
    
    private final SupportMapFragment mMapFragment;
    
    private Context mContext;
    
    
    DetailPresenterImpl(Context context) {
        mContext = context;
        
        tv_hotelName = (TextView) ((Activity) context).findViewById(R.id.tv_hotelName);
        tv_hotelAddress = (TextView) ((Activity) context).findViewById(R.id.tv_hotelAddress);
        tv_hotelDistance = (TextView) ((Activity) context).findViewById(tv_distance);
        tv_suitsAvailable = (TextView) ((Activity) context).findViewById(R.id.tv_suitsAvailable);
        rb_hotelStars = (RatingBar) ((Activity) context).findViewById(R.id.rating_bar_hotel_stars);
        iv_hotelImage = (ImageView) ((Activity) context).findViewById(R.id.iv_hotelImage);
    
        mMapFragment = (SupportMapFragment) ((AppCompatActivity) mContext).getSupportFragmentManager()
                .findFragmentById(R.id.map_hotelLocation);
    }
    
    public void initLoader(LoaderManager manager, Intent intent) {
        manager.initLoader(LOADER_ID_HOTELS, intent.getExtras(), this);
    }
    
    /**
     * Creating loader that pulls hotel info from DB
     *
     * @param id   hotel ID to pull info from
     * @param args any incoming data
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This method generates a selection off of only the current followers
        String selection = HotelContract.COLUMN_HOTEL_ID + "=?";
    
        String[] selectionArgs = new String[]{String.valueOf(args.getInt(HotelContract.COLUMN_HOTEL_ID))};
        return new CursorLoader(mContext, HotelProvider.HotelInfo.CONTENT_URI,
                HOTEL_PROJECTION, selection, selectionArgs, null);
    }
    
    /**
     * Setting up values to textViews.
     *
     * @param data cursor to the table.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            tv_hotelName.setText(data.getString(COL_NUM_HOTEL_NAME));
            tv_hotelAddress.setText(data.getString(COL_NUM_HOTEL_ADDRESS));
        
            tv_hotelDistance.setText(
                    mContext.getString(isMetric(mContext) ? R.string.distance_metric : R.string.distance_imperial,
                            isMetric(mContext) ? data.getFloat(COL_NUM_HOTEL_DISTANCE) * MPH_TO_KPH_COEF : data.getFloat(COL_NUM_HOTEL_DISTANCE)));
        
            String suitsAvailability = data.getString(COL_NUM_HOTEL_SUITS_AVAILABILITY);
        
            tv_suitsAvailable.setText(mContext.getString(R.string.detailed_suits_available, parseSuitsAvailability(suitsAvailability)));
        
            rb_hotelStars.setRating(data.getFloat(COL_NUM_HOTEL_STARS));
        
            mLat = data.getFloat(COL_NUM_HOTEL_LATITUDE);
            mLon = data.getFloat(COL_NUM_HOTEL_LONGITUDE);
        
            //Launching map now
        
            PicassoImageLoader.load(mContext, data.getString(COL_NUM_HOTEL_IMAGE_URL), iv_hotelImage);
    
            mMapFragment.getMapAsync(this);
        }
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(mLat, mLon);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setMinZoomPreference(GOOGLE_MAPS_ZOOM_LEVEL);
    }
    
    /**
     * Checking whether settings has units in metric or imperial
     */
    private boolean isMetric(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.preference_unit), context.getString(R.string.unit_entry_value_imperial))
                .equals(context.getString(R.string.unit_entry_value_metric));
    }
    
    /**
     * Parsing list of suits into readable string.
     */
    private String parseSuitsAvailability(String suitsAvailability) {
        if (suitsAvailability.endsWith(":"))
            suitsAvailability = suitsAvailability.substring(0, suitsAvailability.length() - 1);
        return suitsAvailability.replace(":", ", ");
    }
    
    @Override
    public void onDestroy() {
        mContext = null;
    }
}
