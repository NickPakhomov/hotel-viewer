package com.github.nickpakhomov.hotel_viewer.models

/**
 * Holder object for hotel values
 * Created by Nikolay Pakhomov on 03/05/17.
 */

data class HotelBody(
        val id: Int,
        val name: String,
        val address: String,
        val stars: Float,
        val distance: Float,
        val image: String,
        val suites_availability: String,
        val lat: Float,
        val lon: Float
)
