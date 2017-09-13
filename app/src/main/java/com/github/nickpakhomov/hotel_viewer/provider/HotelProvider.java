package com.github.nickpakhomov.hotel_viewer.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Content provider class. Setting up Authority.
 * Created by Nikolay Pakhomov on 27/08/17.
 */

@ContentProvider(
        authority = HotelProvider.AUTHORITY,
        database = HotelDatabase.class)

public final class HotelProvider {
    
    public static final String AUTHORITY = "com.github.nickpakhomov.cp_test.provider.provider";
    
    
    @TableEndpoint(table = HotelDatabase.HOTEL_INFO)
    public static class HotelInfo {
        
        @ContentUri(
                path = "hotelInfo",
                type = "vnd.android.cursor.dir/hotelInfo",
                defaultSort = HotelContract.COLUMN_ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/hotelInfo");
    }
}