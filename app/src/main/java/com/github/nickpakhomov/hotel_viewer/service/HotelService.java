package com.github.nickpakhomov.hotel_viewer.service;

import com.github.nickpakhomov.hotel_viewer.models.ResponseBody;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Template for HTTP requests.
 * Created by Nikolay Pakhomov on 02/05/17.
 */

public interface HotelService {
    
    @GET("NSArtem/HotelsProject/master/JSON/hotelsList.json")
    Observable<List<ResponseBody>> fetchHotelPreviewsData();
    
    @GET("NSArtem/HotelsProject/master/JSON/{id}.json")
    Observable<ResponseBody> fetchHotelData(@Path("id") int id);
}