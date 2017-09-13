package com.github.nickpakhomov.hotel_viewer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.nickpakhomov.hotel_viewer.R;
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract;


public class DetailActivity extends BaseActivity {
    
    private DetailPresenter mDetailPresenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mDetailPresenter = new DetailPresenterImpl(this);
        
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            // Start the loader with preexisting bundle
            mDetailPresenter.initLoader(getSupportLoaderManager(), intent);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        mFetcherHandler.fetchHotelsData(getIntent().getIntExtra(HotelContract.COLUMN_HOTEL_ID, 0));
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenter.onDestroy();
    }
}
