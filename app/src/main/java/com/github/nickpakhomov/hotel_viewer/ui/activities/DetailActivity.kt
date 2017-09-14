package com.github.nickpakhomov.hotel_viewer.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.github.nickpakhomov.hotel_viewer.R
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract


class DetailActivity : BaseActivity() {

    private lateinit var mDetailPresenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mDetailPresenter = DetailPresenterImpl(this)

        if (intent.extras != null) {
            // Start the loader with preexisting bundle
            mDetailPresenter.initLoader(supportLoaderManager, intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        mFetcherHandler.fetchHotelsData(intent.getIntExtra(HotelContract.COLUMN_HOTEL_ID, 0))
        return result
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDetailPresenter.onDestroy()
    }
}
