package com.github.nickpakhomov.hotel_viewer.ui.recycler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.nickpakhomov.hotel_viewer.R;
import com.github.nickpakhomov.hotel_viewer.provider.HotelContract;
import com.github.nickpakhomov.hotel_viewer.ui.activities.DetailActivity;

/**
 * RecyclerView that populates list of hotels.
 * Created by Nikolay Pakhomov on 26/08/17.
 */

public class HotelsRecyclerView extends RecyclerView.Adapter<HotelsRecyclerView.HotelViewHolder> {
    
    private Cursor mData;
    
    private static final float MPH_TO_KPH_COEF = 1.60934f;
    
    private static final int COL_NUM_HOTEL_ID = 0;
    private static final int COL_NUM_HOTEL_NAME = 1;
    private static final int COL_NUM_HOTEL_ADDRESS = 2;
    private static final int COL_NUM_HOTEL_STARS = 3;
    private static final int COL_NUM_HOTEL_DISTANCE = 4;
    private static final int COL_NUM_HOTEL_SUITS_AVAILABILITY = 5;
    private static final int COL_NUM_HOTEL_ROOMS_LEFT = 6;
    
    @Override
    public HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_list_item, parent, false);
        return new HotelViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(HotelViewHolder holder, int position) {
        holder.bind(position);
    }
    
    public void swapCursor(Cursor newCursor) {
        mData = newCursor;
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.getCount();
    }
    
    class HotelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tv_hotelName;
        final TextView tv_hotelAddress;
        final TextView tv_distance;
        final TextView tv_roomsAvailable;
        final RatingBar rb_hotelStars;
        
        HotelViewHolder(View layoutView) {
            super(layoutView);
            
            tv_hotelName = (TextView) layoutView.findViewById(R.id.tv_hotelName);
            tv_hotelAddress = (TextView) layoutView.findViewById(R.id.tv_hotelAddress);
            tv_distance = (TextView) layoutView.findViewById(R.id.tv_distance);
            tv_roomsAvailable = (TextView) layoutView.findViewById(R.id.tv_suitsAvailable);
            rb_hotelStars = (RatingBar) layoutView.findViewById(R.id.rating_bar_hotel_stars);
            layoutView.setOnClickListener(this);
        }
        
        void bind(final int position) {
            mData.moveToPosition(position);
            
            Context context = itemView.getContext();
            
            tv_hotelName.setText(mData.getString(COL_NUM_HOTEL_NAME));
            tv_hotelAddress.setText(mData.getString(COL_NUM_HOTEL_ADDRESS));
            tv_distance.setText(
                    context.
                            getString(isMetric(context) ? R.string.distance_metric : R.string.distance_imperial,
                                    isMetric(context) ? mData.getFloat(COL_NUM_HOTEL_DISTANCE) * MPH_TO_KPH_COEF : mData.getFloat(COL_NUM_HOTEL_DISTANCE)));
            
            tv_roomsAvailable.setText(context.getString(R.string.main_suits_available, mData.getInt(COL_NUM_HOTEL_ROOMS_LEFT)));
            rb_hotelStars.setRating(mData.getFloat(COL_NUM_HOTEL_STARS));
        }
    
        boolean isMetric(Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(context.getString(R.string.preference_unit), "")
                    .equals(context.getString(R.string.unit_entry_value_metric));
        }
    
        @Override
        public void onClick(View v) {
            mData.moveToPosition(this.getLayoutPosition());
            int hotelID = mData.getInt(COL_NUM_HOTEL_ID);
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra(HotelContract.COLUMN_HOTEL_ID, hotelID);
    
            v.getContext().startActivity(intent);
        }
    }
    
}
