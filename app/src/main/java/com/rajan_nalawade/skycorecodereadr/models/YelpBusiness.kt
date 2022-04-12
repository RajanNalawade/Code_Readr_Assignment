package com.rajan_nalawade.skycorecodereadr.models

import com.google.gson.annotations.SerializedName

data class YelpBusiness(
    @SerializedName("businesses")
    val businesses: List<Businesse>,
    @SerializedName("region")
    val region: Region,
    @SerializedName("total")
    val total: Int
)