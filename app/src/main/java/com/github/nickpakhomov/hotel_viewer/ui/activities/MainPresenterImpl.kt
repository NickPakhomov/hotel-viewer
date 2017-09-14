package com.github.nickpakhomov.hotel_viewer.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.github.nickpakhomov.hotel_viewer.R
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract
import com.github.nickpakhomov.hotel_viewer.provider.HotelProvider
import com.github.nickpakhomov.hotel_viewer.ui.recycler.HotelsRecyclerView

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

internal class MainPresenterImpl(private var mContext: Context) : MainPresenter {
    private var mAdapter: HotelsRecyclerView

    init {

        val mRecyclerView = (mContext as Activity).findViewById(R.id.recycler_view_hotels) as RecyclerView
        //Since changes in content do not change the layout size
        //of container, setting size as fixed
        mRecyclerView.setHasFixedSize(true)

        //Using linear layout manager
        val mLayoutManager = LinearLayoutManager(mContext)
        mRecyclerView.layoutManager = mLayoutManager

        //Adding divider
        val dividerItemDecoration = DividerItemDecoration(
                mRecyclerView.context,
                mLayoutManager.orientation)
        mRecyclerView.addItemDecoration(dividerItemDecoration)

        mAdapter = HotelsRecyclerView()
        mRecyclerView.adapter = mAdapter
    }

    override fun initLoader(manager: LoaderManager, intent: Intent) {
        manager.initLoader(LOADER_ID_HOTELS, Bundle(), this)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> = //Sorted "by distance"
            when (sortingOrder) {
                mContext.getString(R.string.sort_entry_value_distance) -> CursorLoader(mContext, HotelProvider.HotelInfo.CONTENT_URI,
                        HOTEL_PROJECTION, null, null, HotelContract.COLUMN_HOTEL_DISTANCE + " ASC")
            //If sorted "by rooms"
                mContext.getString(R.string.sort_entry_value_rooms_available) -> CursorLoader(mContext, HotelProvider.HotelInfo.CONTENT_URI,
                        HOTEL_PROJECTION, null, null, HotelContract.COLUMN_HOTEL_ROOMS_LEFT + " DESC")
                else
                    //If no sort at all
                -> CursorLoader(mContext, HotelProvider.HotelInfo.CONTENT_URI,
                        HOTEL_PROJECTION, null, null, HotelContract.COLUMN_ID + " ASC")
            }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }

    /**
     * Retrieving sorting order from SharedPrefs
     */
    private val sortingOrder: String
        get() {
            val preferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            return preferences.getString(mContext.getString(R.string.preference_sort), "")
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_references -> {
            val intent = Intent(mContext, PreferenceActivity::class.java)
            mContext.startActivity(intent)
            true
        }
        R.id.action_sort -> {
            createSortDialog()
            true
        }
        else -> false
    }

    /**
     * Opening single choice alert dialog to chose sorting option
     */
    private fun createSortDialog() {
        val mySortAlertDialog = AlertDialog.Builder(mContext)
        mySortAlertDialog.setTitle(mContext.getString(R.string.preference_sort_title))
        val sort_entries = mContext.resources.getStringArray(R.array.sort_entries)
        val sort_entries_values = mContext.resources.getStringArray(R.array.sort_entries_value)

        var checkedItem = -1
        val s = sortingOrder
        if (s == sort_entries_values[0])
            checkedItem = 0
        else if (s == sort_entries_values[1])
            checkedItem = 1

        mySortAlertDialog.setSingleChoiceItems(sort_entries, checkedItem) { dialog, which ->
            val preferences = PreferenceManager.getDefaultSharedPreferences(mContext).edit()
            preferences.putString(mContext.getString(R.string.preference_sort), sort_entries_values[which])
            preferences.apply()

            dialog.dismiss()

            val manager = (mContext as AppCompatActivity).supportLoaderManager
            manager?.restartLoader(LOADER_ID_HOTELS, Bundle(), this)
        }

        mySortAlertDialog
                .create()
                .show()
    }

    override fun onDestroy() {
        /*mContext = null
        mAdapter = null*/
    }

    companion object {

        private val LOADER_ID_HOTELS = 0

        private val HOTEL_PROJECTION = arrayOf(
                HotelContract.COLUMN_HOTEL_ID,
                HotelContract.COLUMN_HOTEL_NAME,
                HotelContract.COLUMN_HOTEL_ADDRESS,
                HotelContract.COLUMN_HOTEL_STARS,
                HotelContract.COLUMN_HOTEL_DISTANCE,
                HotelContract.COLUMN_HOTEL_SUITS_AVAILABILITY,
                HotelContract.COLUMN_HOTEL_ROOMS_LEFT)
    }
}
