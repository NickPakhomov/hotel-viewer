package com.github.nickpakhomov.hotel_viewer.handler;

import android.content.Context;
import android.widget.Toast;

import com.github.nickpakhomov.hotel_viewer.R;
import com.github.nickpakhomov.hotel_viewer.models.ResponseBody;
import com.github.nickpakhomov.hotel_viewer.service.ApiClient;
import com.github.nickpakhomov.hotel_viewer.service.HotelService;

import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Helper class to process HTTP requests along with "refresh" animation
 * Created by Nikolay Pakhomov on 28/08/17.
 */

public class FetcherHandlerImpl implements FetcherHandler {
    private static final String LOG_TAG = FetcherHandlerImpl.class.getSimpleName();
    private AnimationHandlerImpl mAnimationHandler;
    
    private int lastHotelID;
    
    private Context mContext;
    
    private CompositeDisposable mCompositeDisposable;
    private HotelService hotelService;
    private QueryHandler mQueryHandler;
    
    public FetcherHandlerImpl(Context context, AnimationHandlerImpl animationHandler) {
        mContext = context;
        mAnimationHandler = animationHandler;
        
        mCompositeDisposable = new CompositeDisposable();
        hotelService = ApiClient.getClient().create(HotelService.class);
        mQueryHandler = new QueryHandlerImpl(context.getContentResolver());
    }
    
    /**
     * Fetching hotel previews to form a list.
     */
    public void fetchHotelPreviewsData() {
        mAnimationHandler.startAnimation();
        
        mCompositeDisposable.add(hotelService.fetchHotelPreviewsData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponsePreviews, this::handleError));
    }
    
    /**
     * Fetching detailed hotel
     *
     * @param hotelID hotel ID to fetch the info from
     */
    public void fetchHotelsData(int hotelID) {
        lastHotelID = hotelID;
        mAnimationHandler.startAnimation();
        
        mCompositeDisposable.add(hotelService.fetchHotelData(hotelID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseHotel, this::handleError));
    }
    
    /**
     * Callback class to handle Fetch Hotel Previews request.
     */
    private void handleResponsePreviews(List<ResponseBody> responseBodies) {
        mAnimationHandler.stopAnimation();
        mQueryHandler.insert(responseBodies);
    }
    
    /**
     * Callback class to handle Fetch Hotel Data request.
     */
    private void handleResponseHotel(ResponseBody responseBody) {
        mAnimationHandler.stopAnimation();
        mQueryHandler.update(responseBody);
    }
    
    public void handleError(Throwable error) {
        mAnimationHandler.setStatusError();
        if (error instanceof UnknownHostException)
            Toast.makeText(mContext, mContext.getString(R.string.error_internet_connection), Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Returning last fetched hotel ID in order to update it from ActionBar menu_main
     */
    int getLastHotelID() {
        return lastHotelID;
    }
    
    public void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
        mAnimationHandler = null;
        mContext = null;
        hotelService = null;
        mQueryHandler = null;
    }
}
