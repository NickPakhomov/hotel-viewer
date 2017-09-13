package com.github.nickpakhomov.hotel_viewer.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Constants that help applications work with the content URIs.
 * Created by Nikolay Pakhomov on 27/08/17.
 */

public class HotelContract {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";
    
    @DataType(DataType.Type.INTEGER)
    @NotNull
    @Unique
    public static final String COLUMN_HOTEL_ID = "id";
    
    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_HOTEL_NAME = "name";
    
    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_HOTEL_ADDRESS = "address";
    
    @DataType(DataType.Type.REAL)
    @NotNull
    public static final String COLUMN_HOTEL_STARS = "stars";
    
    @DataType(DataType.Type.REAL)
    @NotNull
    public static final String COLUMN_HOTEL_DISTANCE = "distance";
    
    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_HOTEL_IMAGE_URL = "image";
    
    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_HOTEL_SUITS_AVAILABILITY = "suites_availability";
    
    @DataType(DataType.Type.REAL)
    public static final String COLUMN_HOTEL_LATITUDE = "lat";
    
    @DataType(DataType.Type.REAL)
    public static final String COLUMN_HOTEL_LONGITUDE = "lon";
    
    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_HOTEL_ROOMS_LEFT = "rooms_left";
}
