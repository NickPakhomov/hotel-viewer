package com.github.nickpakhomov.hotel_viewer.ui.activities;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;

import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

interface DetailPresenter extends
        LoaderManager.LoaderCallbacks<Cursor>,
        OnMapReadyCallback,
        Presenter {
    void onDestroy();
}
