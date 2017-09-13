package com.github.nickpakhomov.hotel_viewer.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.nickpakhomov.hotel_viewer.R;
import com.github.nickpakhomov.hotel_viewer.handler.AnimationHandlerImpl;
import com.github.nickpakhomov.hotel_viewer.handler.FetcherHandlerImpl;

/**
 * Base activity that creates {@link FetcherHandlerImpl} and {@link AnimationHandlerImpl}
 * <br/>
 * Created by Nikolay Pakhomov on 08/09/17.
 */

public class BaseActivity extends AppCompatActivity {
    
    private AnimationHandlerImpl mAnimationHandler;
    FetcherHandlerImpl mFetcherHandler;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (this instanceof MainActivity)
            inflater.inflate(R.menu.menu_main, menu);
        else if (this instanceof DetailActivity)
            inflater.inflate(R.menu.menu_detailed, menu);
        
        final MenuItem item = menu.findItem(R.id.action_refresh);
        
        mAnimationHandler = new AnimationHandlerImpl(item, this);
        mFetcherHandler = new FetcherHandlerImpl(this, mAnimationHandler);
        mAnimationHandler.setFetcher(mFetcherHandler);
        return true;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFetcherHandler.onDestroy();
        mAnimationHandler.onDestroy();
    }
}
