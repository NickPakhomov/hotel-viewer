package com.github.nickpakhomov.hotel_viewer.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.github.nickpakhomov.hotel_viewer.R
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract
import com.github.nickpakhomov.hotel_viewer.provider.HotelProvider
import com.github.nickpakhomov.hotel_viewer.util.image.PicassoImageLoader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Implementation of presenter layer of DetailActivity
 * Created by Nikolay Pakhomov on 08/09/17.
 */

internal class DetailPresenterImpl(private var mContext: Context) : DetailPresenter {

    private var mLat: Float = 0.toFloat()
    private var mLon: Float = 0.toFloat()

    private val tv_hotelName: TextView = (mContext as Activity).findViewById(R.id.tv_hotelName) as TextView
    private val tv_hotelAddress: TextView = (mContext as Activity).findViewById(R.id.tv_hotelAddress) as TextView
    private val tv_hotelDistance: TextView = (mContext as Activity).findViewById(R.id.tv_distance) as TextView
    private val tv_suitsAvailable: TextView = (mContext as Activity).findViewById(R.id.tv_suitsAvailable) as TextView
    private val rb_hotelStars: RatingBar = (mContext as Activity).findViewById(R.id.rating_bar_hotel_stars) as RatingBar
    private val iv_hotelImage: ImageView = (mContext as Activity).findViewById(R.id.iv_hotelImage) as ImageView

    private val mMapFragment: SupportMapFragment = (mContext as AppCompatActivity).supportFragmentManager
            .findFragmentById(R.id.map_hotelLocation) as SupportMapFragment

    override fun initLoader(manager: LoaderManager, intent: Intent) {
        manager.initLoader(LOADER_ID_HOTELS, intent.extras, this)
    }

    /**
     * Creating loader that pulls hotel info from DB
     *
     * @param id   hotel ID to pull info from
     * @param args any incoming data
     */
    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
        // This method generates a selection off of only the current followers
        val selection = HotelContract.COLUMN_HOTEL_ID + "=?"

        val selectionArgs = arrayOf(args.getInt(HotelContract.COLUMN_HOTEL_ID).toString())
        return CursorLoader(mContext, HotelProvider.HotelInfo.CONTENT_URI,
                HOTEL_PROJECTION, selection, selectionArgs, "")
    }

    /**
     * Setting up values to textViews.
     *
     * @param data cursor to the table.
     */
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null && data.count > 0) {
            data.moveToFirst()
            tv_hotelName.text = data.getString(COL_NUM_HOTEL_NAME)
            tv_hotelAddress.text = data.getString(COL_NUM_HOTEL_ADDRESS)

            tv_hotelDistance.text = mContext.getString(if (isMetric()) R.string.distance_metric else R.string.distance_imperial,
                    if (isMetric()) data.getFloat(COL_NUM_HOTEL_DISTANCE) * MPH_TO_KPH_COEF else data.getFloat(COL_NUM_HOTEL_DISTANCE))

            val suitsAvailability = data.getString(COL_NUM_HOTEL_SUITS_AVAILABILITY)

            tv_suitsAvailable.text = mContext.getString(R.string.detailed_suits_available, parseSuitsAvailability(suitsAvailability))

            rb_hotelStars.rating = data.getFloat(COL_NUM_HOTEL_STARS)

            mLat = data.getFloat(COL_NUM_HOTEL_LATITUDE)
            mLon = data.getFloat(COL_NUM_HOTEL_LONGITUDE)

            //Launching map now

            PicassoImageLoader.load(mContext, data.getString(COL_NUM_HOTEL_IMAGE_URL), iv_hotelImage)

            mMapFragment.getMapAsync(this)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(mLat.toDouble(), mLon.toDouble())
        googleMap.addMarker(MarkerOptions().position(latLng))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.setMinZoomPreference(GOOGLE_MAPS_ZOOM_LEVEL.toFloat())
    }

    /**
     * Checking whether settings has units in metric or imperial
     */
    private fun isMetric(): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        return preferences.getString(mContext.getString(R.string.preference_unit),
                mContext.getString(R.string.unit_entry_value_imperial)) == mContext.getString(R.string.unit_entry_value_metric)
    }

    /**
     * Parsing list of suits into readable string.
     */
    private fun parseSuitsAvailability(roomsAvailable: String): String {
        var suitsAvailability = roomsAvailable
        if (suitsAvailability.endsWith(":"))
            suitsAvailability = suitsAvailability.substring(0, suitsAvailability.length - 1)
        return suitsAvailability.replace(":", ", ")
    }

    override fun onDestroy() {
        //mContext = null
    }

    companion object {

        private val LOG_TAG = DetailPresenterImpl::class.java.simpleName

        private val LOADER_ID_HOTELS = 0

        private val GOOGLE_MAPS_ZOOM_LEVEL = 12
        private val MPH_TO_KPH_COEF = 1.60934f

        private val HOTEL_PROJECTION = arrayOf(HotelContract.COLUMN_HOTEL_ID,
                HotelContract.COLUMN_HOTEL_NAME,
                HotelContract.COLUMN_HOTEL_ADDRESS,
                HotelContract.COLUMN_HOTEL_STARS,
                HotelContract.COLUMN_HOTEL_DISTANCE,
                HotelContract.COLUMN_HOTEL_IMAGE_URL,
                HotelContract.COLUMN_HOTEL_SUITS_AVAILABILITY,
                HotelContract.COLUMN_HOTEL_LATITUDE,
                HotelContract.COLUMN_HOTEL_LONGITUDE)

        private val COL_NUM_HOTEL_ID = 0
        private val COL_NUM_HOTEL_NAME = 1
        private val COL_NUM_HOTEL_ADDRESS = 2
        private val COL_NUM_HOTEL_STARS = 3
        private val COL_NUM_HOTEL_DISTANCE = 4
        private val COL_NUM_HOTEL_IMAGE_URL = 5
        private val COL_NUM_HOTEL_SUITS_AVAILABILITY = 6
        private val COL_NUM_HOTEL_LATITUDE = 7
        private val COL_NUM_HOTEL_LONGITUDE = 8
    }
}
