package com.github.nickpakhomov.hotel_viewer.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to create a database with one
 * table for messages
 */

@Database(version = HotelDatabase.VERSION)
class HotelDatabase {
    
    public static final int VERSION = 10;
    
    @Table(HotelContract.class)
    static final String HOTEL_INFO = "hotel_info";
    
}