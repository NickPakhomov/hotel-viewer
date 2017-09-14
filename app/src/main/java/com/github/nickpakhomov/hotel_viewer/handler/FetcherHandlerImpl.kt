package com.github.nickpakhomov.hotel_viewer.handler

import android.content.Context
import android.widget.Toast
import com.github.nickpakhomov.hotel_viewer.R
import com.github.nickpakhomov.hotel_viewer.models.HotelBody
import com.github.nickpakhomov.hotel_viewer.service.ApiClient
import com.github.nickpakhomov.hotel_viewer.service.HotelService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

/**
 * Helper class to process HTTP requests along with "refresh" animation
 * Created by Nikolay Pakhomov on 28/08/17.
 */

class FetcherHandlerImpl(
        private var mContext: Context,
        private var mAnimationHandler: AnimationHandler) : FetcherHandler {

    /**
     * Returning last fetched hotel ID in order to update it from ActionBar menu_main
     */
    private var lastHotelID: Int = 0

    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private var hotelService: HotelService = ApiClient.client.create(HotelService::class.java)
    private var mQueryHandler: QueryHandler = QueryHandlerImpl(mContext.contentResolver)

    /**
     * Fetching hotel previews to form a list.
     */
    override fun fetchHotelPreviewsData() {
        mAnimationHandler.startAnimation()

        mCompositeDisposable.add(hotelService.fetchHotelPreviewsData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.handleResponsePreviews(it) }, { this.handleError(it) }))
    }

    /**
     * Fetching detailed hotel
     *
     * @param hotelID hotel ID to fetch the info from
     */
    override fun fetchHotelsData(hotelID: Int) {
        lastHotelID = hotelID
        mAnimationHandler.startAnimation()

        mCompositeDisposable.add(hotelService.fetchHotelData(hotelID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.handleResponseHotel(it) }, { this.handleError(it) }))
    }

    /**
     * Callback class to handle Fetch Hotel Previews request.
     */
    private fun handleResponsePreviews(hotelBodies: List<HotelBody>) {
        mAnimationHandler.stopAnimation()
        mQueryHandler.insert(hotelBodies)
    }

    /**
     * Callback class to handle Fetch Hotel Data request.
     */
    private fun handleResponseHotel(hotelBody: HotelBody) {
        mAnimationHandler.stopAnimation()
        mQueryHandler.update(hotelBody)
    }

    override fun handleError(error: Throwable) {
        mAnimationHandler.setStatusError()
        if (error is UnknownHostException)
            Toast.makeText(mContext, mContext.getString(R.string.error_internet_connection), Toast.LENGTH_SHORT).show()
    }

    override fun getLastHotelID(): Int = lastHotelID

    override fun onDestroy() {
        mCompositeDisposable.clear()
    }

    companion object {
        private val LOG_TAG = FetcherHandlerImpl::class.java.simpleName
    }
}
