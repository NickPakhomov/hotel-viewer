package com.github.nickpakhomov.hotel_viewer.handler

import android.content.Context
import android.os.Handler
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.github.nickpakhomov.hotel_viewer.R
import com.github.nickpakhomov.hotel_viewer.ui.activities.DetailActivity
import com.github.nickpakhomov.hotel_viewer.ui.activities.MainActivity

/**
 * Class that handles "refresh" icon animation
 * Created by Nikolay Pakhomov on 28/08/17.
 */

class AnimationHandlerImpl(
        mItem: MenuItem?,
        private var mContext: Context) : AnimationHandler {

    private lateinit var mFetcherHandler: FetcherHandler
    private var mRefreshButton: ImageView
    private lateinit var mAnimationStopHandler: Handler

    init {
        mItem?.setActionView(R.layout.iv_refresh)
        mRefreshButton = mItem?.actionView?.findViewById(R.id.refreshButton) as ImageView
        mRefreshButton.setOnClickListener { update() }
    }

    override fun setFetcher(mFetcherHandler: FetcherHandler) {
        this.mFetcherHandler = mFetcherHandler
    }

    private fun update() {
        stopAnimationImmediately()

        if (mContext is MainActivity)
            mFetcherHandler.fetchHotelPreviewsData()
        else if (mContext is DetailActivity)
            mFetcherHandler.fetchHotelsData(mFetcherHandler.getLastHotelID())
    }

    override fun startAnimation() {
        mRefreshButton.setImageResource(R.drawable.ic_sync_arrows)
        val rotation = AnimationUtils.loadAnimation(mContext, R.anim.refresh_rotation)
        rotation.fillAfter = true
        mRefreshButton.startAnimation(rotation)
    }

    private fun stopAnimationImmediately() {
        mRefreshButton.clearAnimation()
    }

    /**
     * Extending animation to extra 500ms for UX
     */
    override fun stopAnimation() {
        mAnimationStopHandler = Handler()
        mAnimationStopHandler.postDelayed({ mRefreshButton.clearAnimation() },
                ANIMATION_STOP_DELAY.toLong())
    }

    override fun setStatusError() {
        stopAnimation()
        mRefreshButton.setImageResource(R.drawable.ic_sync_arrows_error)
    }

    override fun onDestroy() {
        mAnimationStopHandler.removeCallbacksAndMessages(null)
        stopAnimationImmediately()
    }

    companion object {
        private val ANIMATION_STOP_DELAY = 500 //ms
    }
}
