package com.example.snaplapse.api

import com.example.snaplapse.api.data.UserCredentialsRequest
import com.example.snaplapse.api.data.LoginResult
import com.example.snaplapse.api.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersApi {
    @POST("/api/login/")
    suspend fun login(@Body user: UserCredentialsRequest): Response<LoginResult>

    @POST("/api/users/")
    suspend fun register(@Body user: UserCredentialsRequest): Response<User>
}