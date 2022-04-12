package com.rajan_nalawade.skycorecodereadr.models

import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("center")
    val center: Center
)