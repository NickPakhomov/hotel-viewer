package com.github.nickpakhomov.hotel_viewer.ui.activities

import android.content.Intent
import android.support.v4.app.LoaderManager

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

internal interface Presenter {
    fun initLoader(manager: LoaderManager, intent: Intent)
}
