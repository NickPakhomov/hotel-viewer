package com.github.nickpakhomov.hotel_viewer.handler;

/**
 * Created by Nikolay Pakhomov on 09/09/17.
 */

interface AnimationHandler {
    void setFetcher(FetcherHandlerImpl mFetcherHandler);
    void startAnimation();
    void stopAnimation();
    void setStatusError();
    void onDestroy();
}
