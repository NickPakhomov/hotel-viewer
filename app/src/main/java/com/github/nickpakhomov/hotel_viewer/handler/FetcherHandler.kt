package com.github.nickpakhomov.hotel_viewer.handler

/**
 * Helper class to process HTTP requests along with "refresh" animation
 * Created by Nikolay Pakhomov on 09/09/17.
 */

interface FetcherHandler {

    /**
     * Fetching hotels preview data for initial list
     */
    fun fetchHotelPreviewsData()

    /**
     * Fetching specific hotel's data
     * @param hotelID       hotel ID taken from preview list
     */
    fun fetchHotelsData(hotelID: Int)

    /**
     * Handling reactive error callback
     */
    fun handleError(error: Throwable)

    fun getLastHotelID() : Int

    /**
     * Cleaning up on finish
     */
    fun onDestroy()
}
