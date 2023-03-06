package com.example.snaplapse.api.routes

import com.example.snaplapse.api.data.photo.PhotoActionRequest
import com.example.snaplapse.api.data.photo.PhotoActionResponse
import com.example.snaplapse.api.data.photo.PhotoActionListResponse
import com.example.snaplapse.api.data.photo.PhotoRequest
import com.example.snaplapse.api.data.photo.PhotoResponse
import com.example.snaplapse.api.data.photo.PhotoListResponse
import retrofit2.Response
import retrofit2.http.*

interface PhotosApi {
    @GET("/api/photos/")
    suspend fun getPhotosByUser(
        @Query("user") user: Int,
        @Query("sort_by") sortBy: String? = null,
    ): Response<PhotoListResponse>

    @GET("/api/photos/")
    suspend fun getPhotosByLocation(
        @Query("location") user: Int,
        @Query("sort_by") sortBy: String? = null,
    ): Response<PhotoListResponse>

    @GET("/api/photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: Int?,
    ): Response<PhotoResponse>

    @GET("/api/likes/")
    suspend fun getLike(
        @Query("user") user: Int?,
        @Query("photo") photo: Int?,
    ): Response<PhotoActionListResponse>

    @GET("/api/flags/")
    suspend fun getFlag(
        @Query("user") user: Int,
        @Query("photo") photo: Int,
    ): Response<PhotoActionListResponse>

    @POST("/api/photos/")
    suspend fun upload(
        @Body photo: PhotoRequest,
    ): Response<PhotoResponse>

    @POST("/api/likes/")
    suspend fun like(
        @Body photoActionRequest: PhotoActionRequest,
    ): Response<PhotoActionResponse>

    @POST("/api/flags/")
    suspend fun flag(
        @Body photoActionRequest: PhotoActionRequest,
    ): Response<PhotoActionResponse>

    @PUT("/api/photos/{id}/")
    suspend fun edit(
        @Path("id") id: Int,
    ): Response<PhotoResponse>

    @DELETE("/api/photos/{id}/")
    suspend fun delete(
        @Path("id") id: Int,
    ): Response<String>

    @DELETE("/api/likes/{id}/")
    suspend fun unlike(
        @Path("id") id: Int,
    ): Response<String>

    @DELETE("/api/flags/{id}/")
    suspend fun unflag(
        @Path("id") id: Int,
    ): Response<String>
}