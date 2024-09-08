package com.example.collegeDate.model

import org.springframework.data.annotation.Id

data class CollegeList(
    val collegeName: String,
    @Id
    val id: Int
)
