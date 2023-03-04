package com.example.snaplapse.api.routes

import com.example.snaplapse.api.data.location.LocationsResponse
import com.example.snaplapse.api.data.location.LocationResponse
import retrofit2.Response
import retrofit2.http.*



interface LocationsApi {
    @GET("/api/locations/")
    suspend fun getLocations(): Response<LocationsResponse>

    @GET("/api/locations/{id}")
    suspend fun getLocation(@Path("id") id: Int?): Response<LocationResponse>

    @GET("/api/locations/recommendations")
    suspend fun getRecommendations(@Query("userId") userId: Int,
                                @Query("coordinates", encoded = true) coordinates: String,
                                @Query("radius") radius: Double,
                                @Query("count") count: Int): Response<LocationsResponse>
}
