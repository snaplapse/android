package com.example.snaplapse.api.routes

import com.example.snaplapse.api.data.location.LocationRequest
import com.example.snaplapse.api.data.location.LocationResponse
import retrofit2.Response
import retrofit2.http.*

interface LocationsApi {
    @GET("/api/locations/googleId/{google_id}/")
    suspend fun getLocationByGoogleId(@Path("google_id") google_id: String): Response<LocationResponse>

    @POST("/api/locations/")
    suspend fun createLocation(@Body locationRequest: LocationRequest): Response<LocationResponse>

    @PATCH("/api/locations/{id}")
    suspend fun editLocation(@Path("id") id: Int): Response<String>

    @DELETE("/api/locations/{id}")
    suspend fun delete(@Path("id") id: Int): Response<String>
}