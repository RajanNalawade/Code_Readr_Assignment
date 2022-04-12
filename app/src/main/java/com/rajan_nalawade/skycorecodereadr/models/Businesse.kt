package com.rajan_nalawade.skycorecodereadr.models

import com.google.gson.annotations.SerializedName

data class Businesse(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("coordinates")
    val coordinates: Coordinates,
    @SerializedName("display_phone")
    val displayPhone: String,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("is_closed")
    val isClosed: Boolean,
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("review_count")
    val reviewCount: Int,
    @SerializedName("transactions")
    val transactions: List<String>,
    @SerializedName("url")
    val url: String
)