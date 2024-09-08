package com.example.collegeDate.model

import org.springframework.data.annotation.Id
import java.time.LocalDateTime


data class OtpSavedInDatabase(
    @Id
    val gmail: String,
    val otp: String,
    val time: LocalDateTime
)
