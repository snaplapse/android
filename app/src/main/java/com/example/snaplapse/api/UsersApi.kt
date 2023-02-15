package com.example.snaplapse.api

import com.example.snaplapse.api.data.UserList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {
    @GET("/api/users")
    suspend fun getUsers(): Response<UserList>
}