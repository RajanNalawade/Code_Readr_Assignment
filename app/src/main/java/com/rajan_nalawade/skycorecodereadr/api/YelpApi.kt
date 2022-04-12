package com.rajan_nalawade.skycorecodereadr.api

import com.rajan_nalawade.skycorecodereadr.models.YelpBusiness
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpApi {

    private val barier_toekn: String
        get() = "XPFgzKwZGK1yqRxHi0d5xsARFOLpXIvccQj5jekqTnysweGyoIfVUHcH2tPfGq5Oc9kwKHPkcOjk2d1Xobn7aTjOFeop8x41IUfVvg2Y27KiINjYPADcE7Qza0RkX3Yx"

    @GET("businesses/search")
    suspend fun getRestaurants(
        @Header("Authorization") authHeader: String = "Bearer $barier_toekn",
        @Query("term") term: String = "restaurants",
        @Query("location") location: String = "NYC",
        @Query("limit") limit: String,
        @Query("offset") offset: String,
        @Query("sort_by") sort_by: String = "distance",
        @Query("radious") radious: String
    ): YelpBusiness
}