package com.github.nickpakhomov.hotel_viewer.handler

import com.github.nickpakhomov.hotel_viewer.models.HotelBody

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

internal interface QueryHandler {
    fun insert(list: List<HotelBody>)
    fun update(hotelBody: HotelBody)
}
