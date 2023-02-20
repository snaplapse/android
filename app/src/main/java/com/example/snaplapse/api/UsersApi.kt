package com.example.snaplapse.api

import com.example.snaplapse.api.data.user.UserCredentialsRequest
import com.example.snaplapse.api.data.user.LoginResult
import com.example.snaplapse.api.data.user.User
import retrofit2.Response
import retrofit2.http.*

interface UsersApi {
    @POST("/api/login/")
    suspend fun login(@Body user: UserCredentialsRequest): Response<LoginResult>

    @POST("/api/users/")
    suspend fun register(@Body user: UserCredentialsRequest): Response<User>

    @PUT("/api/edituser/{id}/")
    suspend fun edit(@Path("id") id: String, @Body user: UserCredentialsRequest): Response<String>

    @DELETE("/api/users/{id}/")
    suspend fun delete(@Path("id") id: String): Response<String>
}