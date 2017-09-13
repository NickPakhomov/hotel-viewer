package com.github.nickpakhomov.hotel_viewer.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.github.nickpakhomov.hotel_viewer.R;
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract;
import com.github.nickpakhomov.hotel_viewer.provider.HotelProvider;
import com.github.nickpakhomov.hotel_viewer.ui.recycler.HotelsRecyclerView;

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

class MainPresenterImpl implements MainPresenter {
    
    private static final int LOADER_ID_HOTELS = 0;
    private HotelsRecyclerView mAdapter;
    
    private static final String[] HOTEL_PROJECTION = {
            HotelContract.COLUMN_HOTEL_ID,
            HotelContract.COLUMN_HOTEL_NAME,
            HotelContract.COLUMN_HOTEL_ADDRESS,
            HotelContract.COLUMN_HOTEL_STARS,
            HotelContract.COLUMN_HOTEL_DISTANCE,
            HotelContract.COLUMN_HOTEL_SUITS_AVAILABILITY,
            HotelContract.COLUMN_HOTEL_ROOMS_LEFT
    };
    
    private Context mContext;
    
    MainPresenterImpl(Context context) {
        mContext = context;
    
        RecyclerView mRecyclerView = (RecyclerView) ((Activity) mContext).findViewById(R.id.recycler_view_hotels);
        //Since changes in content do not change the layout size
        //of container, setting size as fixed
        mRecyclerView.setHasFixedSize(true);
    
        //Using linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
    
        //Adding divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    
        mAdapter = new HotelsRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
    }
    
    @Override
    public void initLoader(LoaderManager manager, Intent intent) {
        manager.initLoader(LOADER_ID_HOTELS, null, this);
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Sorted "by distance"
        if (getSortingOrder().equals(mContext.getString(R.string.sort_entry_value_distance)))
            return new CursorLoader(mContext, HotelProvider.HotelInfo.CONTENT_URI,
                    HOTEL_PROJECTION, null, null, HotelContract.COLUMN_HOTEL_DISTANCE + " ASC");
        else
            //If sorted "by rooms"
            if (getSortingOrder().equals(mContext.getString(R.string.sort_entry_value_rooms_available)))
            return new CursorLoader(mContext, HotelProvider.HotelInfo.CONTENT_URI,
                    HOTEL_PROJECTION, null, null, HotelContract.COLUMN_HOTEL_ROOMS_LEFT + " DESC");
        else
            //If no sort at all
            return new CursorLoader(mContext, HotelProvider.HotelInfo.CONTENT_URI,
                    HOTEL_PROJECTION, null, null, HotelContract.COLUMN_ID + " ASC");
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
    
    /**
     * Retrieving sorting order from SharedPrefs
     */
    private String getSortingOrder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(mContext.getString(R.string.preference_sort), "");
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_references:
                Intent intent = new Intent(mContext, PreferenceActivity.class);
                mContext.startActivity(intent);
                return true;
            case R.id.action_sort:
                createSortDialog();
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Opening single choice alert dialog to chose sorting option
     */
    private void createSortDialog() {
        AlertDialog.Builder mySortAlertDialog = new AlertDialog.Builder(mContext);
        mySortAlertDialog.setTitle(mContext.getString(R.string.preference_sort_title));
        String[] sort_entries = mContext.getResources().getStringArray(R.array.sort_entries);
        String[] sort_entries_values = mContext.getResources().getStringArray(R.array.sort_entries_value);
        
        int checkedItem = -1;
        String s = getSortingOrder();
        if (s.equals(sort_entries_values[0]))
            checkedItem = 0;
        else if (s.equals(sort_entries_values[1]))
            checkedItem = 1;
        
        mySortAlertDialog.setSingleChoiceItems(sort_entries, checkedItem, (dialog, which) -> {
            SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
            preferences.putString(mContext.getString(R.string.preference_sort), sort_entries_values[which]);
            preferences.apply();
            
            dialog.dismiss();
            
            LoaderManager manager = ((AppCompatActivity) mContext).getSupportLoaderManager();
            if (manager != null)
                manager.restartLoader(LOADER_ID_HOTELS, null, this);
        });
        
        mySortAlertDialog
                .create()
                .show();
    }
    
    @Override
    public void onDestroy() {
        mContext = null;
        mAdapter = null;
    }
}
