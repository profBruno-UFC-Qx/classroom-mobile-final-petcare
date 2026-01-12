package com.example.petcare.data.remote.api

import com.google.gson.annotations.SerializedName

data class OverpassResponse(
    @SerializedName("elements")
    val elements: List<Element>
)

data class Element(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("tags")
    val tags: Map<String, String>?
)