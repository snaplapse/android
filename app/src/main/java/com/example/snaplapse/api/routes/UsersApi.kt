package com.example.snaplapse.api.routes

import com.example.snaplapse.api.data.user.UserCredentialsRequest
import com.example.snaplapse.api.data.GenericResult
import com.example.snaplapse.api.data.user.User
import retrofit2.Response
import retrofit2.http.*

interface UsersApi {
    @POST("/api/login/")
    suspend fun login(@Body user: UserCredentialsRequest): Response<GenericResult>

    @POST("/api/users/")
    suspend fun register(@Body user: UserCredentialsRequest): Response<User>

    @PATCH("/api/users/{id}/")
    suspend fun edit(@Path("id") id: String, @Body user: UserCredentialsRequest): Response<GenericResult>

    @DELETE("/api/users/{id}/")
    suspend fun delete(@Path("id") id: String): Response<String>
}
