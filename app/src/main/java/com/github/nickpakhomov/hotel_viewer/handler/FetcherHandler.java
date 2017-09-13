package com.github.nickpakhomov.hotel_viewer.handler;

/**
 * Created by Nikolay Pakhomov on 09/09/17.
 */

interface FetcherHandler {
    void fetchHotelPreviewsData();
    void fetchHotelsData(int hotelID);
    void handleError(Throwable error);
    void onDestroy();
}
