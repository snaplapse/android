package com.example.snaplapse.api.data.photo

data class PhotosResponse(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<PhotoResponse>
)
