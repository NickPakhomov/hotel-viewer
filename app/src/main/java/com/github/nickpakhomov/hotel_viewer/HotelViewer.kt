package com.github.nickpakhomov.hotel_viewer

import android.app.Application

import com.squareup.leakcanary.LeakCanary

/**
 * Created by Nikolay Pakhomov on 31/08/17.
 */

class HotelViewer : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }
}
