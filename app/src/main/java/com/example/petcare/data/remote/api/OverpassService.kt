package com.example.petcare.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface OverpassService {
    @GET("api/interpreter")
    suspend fun findPetShops(
        @Query("data") query: String
    ): OverpassResponse
}