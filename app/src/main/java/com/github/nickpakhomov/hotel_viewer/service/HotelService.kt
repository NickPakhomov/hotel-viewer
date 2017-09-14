package com.github.nickpakhomov.hotel_viewer.service

import com.github.nickpakhomov.hotel_viewer.models.HotelBody

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Template for HTTP requests.
 * Created by Nikolay Pakhomov on 02/05/17.
 */

interface HotelService {

    @GET("NSArtem/HotelsProject/master/JSON/hotelsList.json")
    fun fetchHotelPreviewsData(): Observable<List<HotelBody>>

    @GET("NSArtem/HotelsProject/master/JSON/{id}.json")
    fun fetchHotelData(@Path("id") id: Int): Observable<HotelBody>
}