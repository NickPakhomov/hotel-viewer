package com.github.nickpakhomov.hotel_viewer.handler;

import android.content.Context;
import android.os.Handler;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.github.nickpakhomov.hotel_viewer.ui.activities.DetailActivity;
import com.github.nickpakhomov.hotel_viewer.ui.activities.MainActivity;
import com.github.nickpakhomov.hotel_viewer.R;

/**
 * Class that handles "refresh" icon animation
 * Created by Nikolay Pakhomov on 28/08/17.
 */

public class AnimationHandlerImpl implements AnimationHandler {
    private static final int ANIMATION_STOP_DELAY = 500; //ms
    
    private MenuItem mItem;
    private Context mContext;
    private FetcherHandlerImpl mFetcherHandler;
    private ImageView mRefreshButton;
    private Handler mAnimationStopHandler;
    
    public AnimationHandlerImpl(MenuItem item, Context context) {
        mItem = item;
        mContext = context;
        
        initAnimationHandler();
    }
    
    public void setFetcher(FetcherHandlerImpl mFetcherHandler) {
        this.mFetcherHandler = mFetcherHandler;
    }
    
    private void initAnimationHandler() {
        mItem.setActionView(R.layout.iv_refresh);
        mRefreshButton = (ImageView) mItem.getActionView().findViewById(R.id.refreshButton);
        mRefreshButton.setOnClickListener(v -> update());
    }
    
    private void update() {
        stopAnimationImmediately();
        
        if (mContext instanceof MainActivity)
            mFetcherHandler.fetchHotelPreviewsData();
        else if (mContext instanceof DetailActivity)
            mFetcherHandler.fetchHotelsData(mFetcherHandler.getLastHotelID());
    }
    
    public void startAnimation() {
        mRefreshButton.setImageResource(R.drawable.ic_sync_arrows);
        Animation rotation = AnimationUtils.loadAnimation(mContext, R.anim.refresh_rotation);
        rotation.setFillAfter(true);
        mRefreshButton.startAnimation(rotation);
    }
    
    private void stopAnimationImmediately() {
        if (mRefreshButton != null)
            mRefreshButton.clearAnimation();
    }
    
    /**
     * Extending animation to extra 500ms for UX
     */
    public void stopAnimation() {
        mAnimationStopHandler =  new Handler();
        mAnimationStopHandler.
                postDelayed(() -> mRefreshButton.clearAnimation(), ANIMATION_STOP_DELAY);
    }
    
    public void setStatusError() {
        stopAnimation();
        mRefreshButton.setImageResource(R.drawable.ic_sync_arrows_error);
    }
    
    public void onDestroy() {
        mItem = null;
        mContext = null;
        if (mAnimationStopHandler != null) {
            mAnimationStopHandler.removeCallbacksAndMessages(null);
            mAnimationStopHandler = null;
        }
        stopAnimationImmediately();
        mRefreshButton = null;
    }
}
