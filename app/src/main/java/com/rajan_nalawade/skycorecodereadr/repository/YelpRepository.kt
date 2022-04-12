package com.rajan_nalawade.skycorecodereadr.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.rajan_nalawade.skycorecodereadr.api.YelpApi
import com.rajan_nalawade.skycorecodereadr.paging.RestaurantsPagingSource

class YelpRepository(private val mYelpApi: YelpApi) {

    fun getRestaurants(city: String, radious: Int) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { RestaurantsPagingSource(city, radious, mYelpApi) }
    ).liveData
}