package com.example.snaplapse.api.data

data class UserList(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<User>
)
