package com.github.nickpakhomov.hotel_viewer.handler

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

import android.content.AsyncQueryHandler
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.github.nickpakhomov.hotel_viewer.models.HotelBody
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract
import com.github.nickpakhomov.hotel_viewer.provider.HotelProvider

/**
 * Handler class that takes care of queries to DB.
 */
class QueryHandlerImpl(cr: ContentResolver) : AsyncQueryHandler(cr), QueryHandler {

    override fun insert(list: List<HotelBody>) {
        for (i in list.indices) {
            val responseBody = list[i]
            val selection = HotelContract.COLUMN_HOTEL_ID + "=?"
            val selectionArgs = arrayOf(responseBody.id.toString())

            startQuery(QUERY_TOKEN, responseBody, HotelProvider.HotelInfo.CONTENT_URI, arrayOf<String>(), selection, selectionArgs, "")
        }
    }

    override fun update(hotelBody: HotelBody) {
        val contentValues = ContentValues()
        contentValues.put(HotelContract.COLUMN_HOTEL_IMAGE_URL, hotelBody.image)
        contentValues.put(HotelContract.COLUMN_HOTEL_LATITUDE, hotelBody.lat)
        contentValues.put(HotelContract.COLUMN_HOTEL_LONGITUDE, hotelBody.lon)

        val selection = HotelContract.COLUMN_HOTEL_ID + "=?"
        val selectionArgs = arrayOf(hotelBody.id.toString())
        startUpdate(UPDATE_TOKEN, Object(), HotelProvider.HotelInfo.CONTENT_URI, contentValues, selection, selectionArgs)
    }

    override fun onUpdateComplete(token: Int, cookie: Any, result: Int) {
        if (token == UPDATE_TOKEN)
            Log.d(LOG_TAG, "Update successful")
        else {
            Log.d(LOG_TAG, "Update failed")
        }
    }

    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
        if (token == QUERY_TOKEN && cursor != null && cursor.count == 0) {
            if (cookie is HotelBody) {
                val contentValues = ContentValues()
                contentValues.put(HotelContract.COLUMN_HOTEL_ID, cookie.id)
                contentValues.put(HotelContract.COLUMN_HOTEL_NAME, cookie.name)
                contentValues.put(HotelContract.COLUMN_HOTEL_ADDRESS, cookie.address)
                contentValues.put(HotelContract.COLUMN_HOTEL_DISTANCE, cookie.distance)
                contentValues.put(HotelContract.COLUMN_HOTEL_SUITS_AVAILABILITY, cookie.suites_availability)
                contentValues.put(HotelContract.COLUMN_HOTEL_STARS, cookie.stars)
                contentValues.put(HotelContract.COLUMN_HOTEL_ROOMS_LEFT,
                        cookie.suites_availability.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size)

                startInsert(INSERT_TOKEN, Object(), HotelProvider.HotelInfo.CONTENT_URI, contentValues)
            }
        }
    }

    override fun onInsertComplete(token: Int, cookie: Any, uri: Uri) {
        if (token == INSERT_TOKEN)
            Log.d(LOG_TAG, "Insertion successful")
        else {
            Log.d(LOG_TAG, "Insertion failed")
        }
    }

    companion object {

        private val LOG_TAG = QueryHandlerImpl::class.java.simpleName

        private val QUERY_TOKEN = 12
        private val INSERT_TOKEN = 11
        private val UPDATE_TOKEN = 1
    }
}