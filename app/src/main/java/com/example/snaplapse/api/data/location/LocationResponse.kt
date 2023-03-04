package com.example.snaplapse.api.data.location

data class LocationResponse(
    val id: Int,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val photos: Array<Int>,
    val tags: Array<Int>,
    val categories: Array<Int>,
    val created: String
)
