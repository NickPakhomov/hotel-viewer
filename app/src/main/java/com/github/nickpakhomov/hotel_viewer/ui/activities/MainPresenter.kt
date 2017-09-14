package com.github.nickpakhomov.hotel_viewer.ui.activities

import android.database.Cursor
import android.support.v4.app.LoaderManager
import android.view.MenuItem

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

internal interface MainPresenter : LoaderManager.LoaderCallbacks<Cursor>, Presenter {
    fun onOptionsItemSelected(item: MenuItem): Boolean

    fun onDestroy()

}
