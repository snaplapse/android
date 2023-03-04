package com.example.snaplapse.api.routes

import com.example.snaplapse.api.data.location.LocationListResponse
import com.example.snaplapse.api.data.location.LocationResponse
import retrofit2.Response
import retrofit2.http.*



interface LocationsApi {
    @GET("/api/locations/")
    suspend fun getLocations(): Response<LocationListResponse>

    @GET("/api/locations/{id}")
    suspend fun getLocation(@Path("id") id: Int?): Response<LocationResponse>

    @GET("/api/locations/recommendations")
    suspend fun getRecommendations(@Query("userId") userId: Int,
                                @Query("coordinates", encoded = true) coordinates: String,
                                @Query("radius") radius: Double,
                                @Query("count") count: Int): Response<LocationListResponse>
}
