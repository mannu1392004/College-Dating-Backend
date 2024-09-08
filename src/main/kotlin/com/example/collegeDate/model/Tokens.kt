package com.example.collegeDate.model

data class Tokens(
    val accessToken: String,
    val refreshToken: String,
    val status: String,
    val step: Int = 4
)
