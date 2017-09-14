package com.github.nickpakhomov.hotel_viewer.provider

import net.simonvt.schematic.annotation.*

/**
 * Constants that help applications work with the content URIs.
 * Created by Nikolay Pakhomov on 27/08/17.
 */

object HotelContract {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    const val COLUMN_ID = "_id"

    @DataType(DataType.Type.INTEGER)
    @NotNull
    @Unique
    const val COLUMN_HOTEL_ID = "id"

    @DataType(DataType.Type.TEXT)
    @NotNull
    const val COLUMN_HOTEL_NAME = "name"

    @DataType(DataType.Type.TEXT)
    @NotNull
    const val COLUMN_HOTEL_ADDRESS = "address"

    @DataType(DataType.Type.REAL)
    @NotNull
    const val COLUMN_HOTEL_STARS = "stars"

    @DataType(DataType.Type.REAL)
    @NotNull
    const val COLUMN_HOTEL_DISTANCE = "distance"

    @DataType(DataType.Type.TEXT)
    const val COLUMN_HOTEL_IMAGE_URL = "image"

    @DataType(DataType.Type.TEXT)
    @NotNull
    const val COLUMN_HOTEL_SUITS_AVAILABILITY = "suites_availability"

    @DataType(DataType.Type.REAL)
    const val COLUMN_HOTEL_LATITUDE = "lat"

    @DataType(DataType.Type.REAL)
    const val COLUMN_HOTEL_LONGITUDE = "lon"

    @DataType(DataType.Type.INTEGER)
    @NotNull
    const val COLUMN_HOTEL_ROOMS_LEFT = "rooms_left"
}
