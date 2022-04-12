package com.rajan_nalawade.skycorecodereadr.utils

import android.Manifest

object Constants {
    const val BASE_URL = "https://api.yelp.com/v3/"

    val arrPermissionRuntime = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    const val LOCATION_PERMISSION_REQ_CODE = 100
    const val GPS_REQUEST = 200
}