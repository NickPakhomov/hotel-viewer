package com.github.nickpakhomov.hotel_viewer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.github.nickpakhomov.hotel_viewer.R

class MainActivity : BaseActivity() {
    private var mBackPressed: Long = 0

    private lateinit var mMainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mMainPresenter = MainPresenterImpl(this)

    }

    override fun onResume() {
        super.onResume()
        mMainPresenter.initLoader(supportLoaderManager, Intent())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        onMenuReady()
        return result
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            mMainPresenter.onOptionsItemSelected(item)

    override fun onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(baseContext, R.string.exit_notification, Toast.LENGTH_SHORT).show()
        }

        mBackPressed = System.currentTimeMillis()
    }

    /**
     * When menu_main "refresh" icon is populated, we can now fetch
     * hotel data with animation.
     */
    private fun onMenuReady() {
        if (mIsFirstTimeLoad) {
            mFetcherHandler.fetchHotelPreviewsData()
            mIsFirstTimeLoad = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainPresenter.onDestroy()
    }

    companion object {

        private val TIME_INTERVAL = 2000 // # milliseconds, desired time passed between two back presses.
        private var mIsFirstTimeLoad = true
    }
}
