package com.github.nickpakhomov.hotel_viewer.handler

/**
 * Created by Nikolay Pakhomov on 09/09/17.
 */

interface AnimationHandler {

    /**
     * Setting reference to HTTP requests fetcher
     */
    fun setFetcher(mFetcherHandler: FetcherHandler)

    /**
     * Starting the HTTP request status animation
     */
    fun startAnimation()

    /**
     * Stopping the HTTP request status animation with certain delay
     */
    fun stopAnimation()

    /**
     * Setting up HTTP requests status icon in error mode
     */
    fun setStatusError()

    /**
     * Cleaning up on finish
     */
    fun onDestroy()
}
