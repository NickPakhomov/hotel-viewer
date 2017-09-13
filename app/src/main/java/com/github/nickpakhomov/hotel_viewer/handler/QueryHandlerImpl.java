package com.github.nickpakhomov.hotel_viewer.handler;

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.github.nickpakhomov.hotel_viewer.models.ResponseBody;
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract;
import com.github.nickpakhomov.hotel_viewer.provider.HotelProvider;

import java.util.List;

/**
 * Handler class that takes care of queries to DB.
 */
public class QueryHandlerImpl extends AsyncQueryHandler implements QueryHandler {
    
    private static final String LOG_TAG = QueryHandlerImpl.class.getSimpleName();
    
    private static final int QUERY_TOKEN = 12;
    private static final int INSERT_TOKEN = 11;
    private static final int UPDATE_TOKEN = 1;
    
    public QueryHandlerImpl(ContentResolver cr) {
        super(cr);
    }
    
    public void insert(List<ResponseBody> responseList) {
        for (int i = 0; i < responseList.size(); i++) {
            ResponseBody responseBody = responseList.get(i);
            String selection = HotelContract.COLUMN_HOTEL_ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(responseBody.getId())};
            
            startQuery(QUERY_TOKEN, responseBody, HotelProvider.HotelInfo.CONTENT_URI, null, selection, selectionArgs, null);
        }
    }
    
    public void update(ResponseBody response) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HotelContract.COLUMN_HOTEL_IMAGE_URL, response.getImage());
        contentValues.put(HotelContract.COLUMN_HOTEL_LATITUDE, response.getLat());
        contentValues.put(HotelContract.COLUMN_HOTEL_LONGITUDE, response.getLon());
        
        String selection = HotelContract.COLUMN_HOTEL_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(response.getId())};
        startUpdate(UPDATE_TOKEN, null, HotelProvider.HotelInfo.CONTENT_URI, contentValues, selection, selectionArgs);
    }
    
    protected void onUpdateComplete(int token, Object cookie, int result) {
        if (token == UPDATE_TOKEN)
            Log.d(LOG_TAG, "Update successful");
        else {
            Log.d(LOG_TAG, "Update failed");
        }
    }
    
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (token == QUERY_TOKEN && cursor != null && cursor.getCount() == 0) {
            if (cookie instanceof ResponseBody) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(HotelContract.COLUMN_HOTEL_ID, ((ResponseBody) cookie).getId());
                contentValues.put(HotelContract.COLUMN_HOTEL_NAME, ((ResponseBody) cookie).getName());
                contentValues.put(HotelContract.COLUMN_HOTEL_ADDRESS, ((ResponseBody) cookie).getAddress());
                contentValues.put(HotelContract.COLUMN_HOTEL_DISTANCE, ((ResponseBody) cookie).getDistance());
                contentValues.put(HotelContract.COLUMN_HOTEL_SUITS_AVAILABILITY, ((ResponseBody) cookie).getSuitesAvailability());
                contentValues.put(HotelContract.COLUMN_HOTEL_STARS, ((ResponseBody) cookie).getStars());
                contentValues.put(HotelContract.COLUMN_HOTEL_ROOMS_LEFT, ((ResponseBody) cookie).getSuitesAvailability().split(":").length);
                
                startInsert(INSERT_TOKEN, null, HotelProvider.HotelInfo.CONTENT_URI, contentValues);
            }
        }
    }
    
    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        if (token == INSERT_TOKEN)
            Log.d(LOG_TAG, "Insertion successful");
        else {
            Log.d(LOG_TAG, "Insertion failed");
        }
    }
}