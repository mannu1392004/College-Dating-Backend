package com.example.collegeDate.model

import org.springframework.data.annotation.Id


data class EmailInvalidToken(
    @Id
    val token: String)
