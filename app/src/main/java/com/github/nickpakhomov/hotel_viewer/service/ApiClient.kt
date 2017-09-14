package com.github.nickpakhomov.hotel_viewer.service

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit client to create HTTP requests.
 * Created by Nikolay Pakhomov on 02/05/17.
 */

object ApiClient {

    private const val BASE_URL = "https://raw.githubusercontent.com/"
    private val mInstance: Retrofit

    init {
        mInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    val client: Retrofit
        get() = mInstance
}
