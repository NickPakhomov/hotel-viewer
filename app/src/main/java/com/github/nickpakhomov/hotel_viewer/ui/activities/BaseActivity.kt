package com.github.nickpakhomov.hotel_viewer.ui.activities

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.github.nickpakhomov.hotel_viewer.R
import com.github.nickpakhomov.hotel_viewer.handler.AnimationHandler
import com.github.nickpakhomov.hotel_viewer.handler.AnimationHandlerImpl
import com.github.nickpakhomov.hotel_viewer.handler.FetcherHandler
import com.github.nickpakhomov.hotel_viewer.handler.FetcherHandlerImpl

/**
 * Base activity that creates [FetcherHandlerImpl] and [AnimationHandlerImpl]
 * <br/>
 * Created by Nikolay Pakhomov on 08/09/17.
 */

open class BaseActivity : AppCompatActivity() {

    private lateinit var mAnimationHandler: AnimationHandler
    internal lateinit var mFetcherHandler: FetcherHandler

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (this is MainActivity)
            menuInflater.inflate(R.menu.menu_main, menu)
        else if (this is DetailActivity)
            menuInflater.inflate(R.menu.menu_detailed, menu)

        val item = menu.findItem(R.id.action_refresh)

        mAnimationHandler = AnimationHandlerImpl(item, this)
        mFetcherHandler = FetcherHandlerImpl(this, mAnimationHandler)
        mAnimationHandler.setFetcher(mFetcherHandler)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mFetcherHandler.onDestroy()
        mAnimationHandler.onDestroy()
    }
}
