package com.rajan_nalawade.skycorecodereadr.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rajan_nalawade.skycorecodereadr.api.YelpApi
import com.rajan_nalawade.skycorecodereadr.models.Businesse

class RestaurantsPagingSource(
    private val city: String,
    private val radious: Int,
    private val mYelpAPI: YelpApi
) :
    PagingSource<Int, Businesse>() {

    private val INITAIL_OFFSET_VALUE = 0
    private val PAGE_LIMIT = 15

    override fun getRefreshKey(state: PagingState<Int, Businesse>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(PAGE_LIMIT) ?: anchorPage?.nextKey?.minus(PAGE_LIMIT)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Businesse> {

        return try {
            val position = params.key ?: INITAIL_OFFSET_VALUE
            val offsetValue =
                if (params.key != null) (position + PAGE_LIMIT) else INITAIL_OFFSET_VALUE
            val response = mYelpAPI.getRestaurants(
                location = city,
                limit = PAGE_LIMIT.toString(),
                offset = offsetValue.toString(),
                radious = radious.toString()
            )

            val nextKey = if (response == null) {
                null
            } else {
                position + PAGE_LIMIT
            }

            return LoadResult.Page(
                data = response.businesses,
                prevKey = null,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}