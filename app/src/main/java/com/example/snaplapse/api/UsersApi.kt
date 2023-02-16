package com.example.snaplapse.api

import com.example.snaplapse.api.data.LoginRequest
import com.example.snaplapse.api.data.User
import com.example.snaplapse.api.data.UserList
import okhttp3.RequestBody
import org.json.JSONObject
import org.json.JSONStringer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsersApi {
    @POST("/api/login/")
    suspend fun login(@Body user: LoginRequest): Response<User>
}