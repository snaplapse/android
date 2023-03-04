package com.example.snaplapse.api.data.location

data class LocationResponse(
    val id: Int,
    val name: String,
    val longitude: Float,
    val latitude: Float,
    val photos: List<Int>,
    val tags: List<Int>,
    val categories: List<Int>,
    val created: String,
    val google_id: String
)
