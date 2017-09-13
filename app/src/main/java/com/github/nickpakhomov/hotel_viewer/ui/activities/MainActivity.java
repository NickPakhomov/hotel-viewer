package com.github.nickpakhomov.hotel_viewer.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.nickpakhomov.hotel_viewer.R;

public class MainActivity extends BaseActivity {
    
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private static boolean mIsFirstTimeLoad = true;
    
    private MainPresenter mMainPresenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        mMainPresenter = new MainPresenterImpl(this);
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.initLoader(getSupportLoaderManager(), null);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        onMenuReady();
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mMainPresenter.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), R.string.exit_notification, Toast.LENGTH_SHORT).show();
        }
        
        mBackPressed = System.currentTimeMillis();
    }
    
    /**
     * When menu_main "refresh" icon is populated, we can now fetch
     * hotel data with animation.
     */
    private void onMenuReady() {
        if (mIsFirstTimeLoad) {
            mFetcherHandler.fetchHotelPreviewsData();
            mIsFirstTimeLoad = false;
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.onDestroy();
    }
}
