package com.github.nickpakhomov.hotel_viewer.ui.recycler

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.github.nickpakhomov.hotel_viewer.R
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract
import com.github.nickpakhomov.hotel_viewer.ui.activities.DetailActivity

/**
 * RecyclerView that populates list of hotels.
 * Created by Nikolay Pakhomov on 26/08/17.
 */

class HotelsRecyclerView : RecyclerView.Adapter<HotelsRecyclerView.HotelViewHolder>() {

    private var mData: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.hotel_list_item, parent, false)
        return HotelViewHolder(v)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        holder.bind(position)
    }

    fun swapCursor(newCursor: Cursor?) {
        mData = newCursor
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mData?.count ?: 0

    inner class HotelViewHolder(layoutView: View) : RecyclerView.ViewHolder(layoutView), View.OnClickListener {
        private val tv_hotelName: TextView = layoutView.findViewById(R.id.tv_hotelName) as TextView
        private val tv_hotelAddress: TextView = layoutView.findViewById(R.id.tv_hotelAddress) as TextView
        private val tv_distance: TextView = layoutView.findViewById(R.id.tv_distance) as TextView
        private val tv_roomsAvailable: TextView = layoutView.findViewById(R.id.tv_suitsAvailable) as TextView
        private val rb_hotelStars: RatingBar = layoutView.findViewById(R.id.rating_bar_hotel_stars) as RatingBar

        init {
            layoutView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            mData!!.moveToPosition(position)

            val context = itemView.context

            tv_hotelName.text = mData!!.getString(COL_NUM_HOTEL_NAME)
            tv_hotelAddress.text = mData!!.getString(COL_NUM_HOTEL_ADDRESS)
            tv_distance.text = context.getString(if (isMetric(context)) R.string.distance_metric else R.string.distance_imperial,
                    if (isMetric(context)) mData!!.getFloat(COL_NUM_HOTEL_DISTANCE) * MPH_TO_KPH_COEF else mData!!.getFloat(COL_NUM_HOTEL_DISTANCE))

            tv_roomsAvailable.text = context.getString(R.string.main_suits_available, mData!!.getInt(COL_NUM_HOTEL_ROOMS_LEFT))
            rb_hotelStars.rating = mData!!.getFloat(COL_NUM_HOTEL_STARS)
        }

        private fun isMetric(context: Context): Boolean {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(context.getString(R.string.preference_unit), "") == context.getString(R.string.unit_entry_value_metric)
        }

        override fun onClick(v: View) {
            mData!!.moveToPosition(this.layoutPosition)
            val hotelID = mData!!.getInt(COL_NUM_HOTEL_ID)
            val intent = Intent(v.context, DetailActivity::class.java)
            intent.putExtra(HotelContract.COLUMN_HOTEL_ID, hotelID)

            v.context.startActivity(intent)
        }
    }

    companion object {

        private val MPH_TO_KPH_COEF = 1.60934f

        private val COL_NUM_HOTEL_ID = 0
        private val COL_NUM_HOTEL_NAME = 1
        private val COL_NUM_HOTEL_ADDRESS = 2
        private val COL_NUM_HOTEL_STARS = 3
        private val COL_NUM_HOTEL_DISTANCE = 4
        private val COL_NUM_HOTEL_SUITS_AVAILABILITY = 5
        private val COL_NUM_HOTEL_ROOMS_LEFT = 6
    }

}
