package com.example.collegeDate.service.database

import com.example.collegeDate.model.OtpSavedInDatabase
import com.example.collegeDate.repository.OtpDatabase
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthDatabaseService(
    private val  otpDatabase: OtpDatabase
) {




    // saving the otp in database
    fun saveOtpInDatabase(
        email: String,
        otp: String,
    ) {
        otpDatabase.save(
            OtpSavedInDatabase(
                otp = otp,
                gmail = email,
                time = LocalDateTime.now()
            )
        )

    }

    fun verifyOtpInDatabase(email: String, otp: String): Boolean {
        val otpSavedInDatabase = this.otpDatabase.findById(email).orElse(null)
        return otpSavedInDatabase != null && otpSavedInDatabase.otp == otp  && otpSavedInDatabase.time.plusMinutes(5).isAfter(LocalDateTime.now())
    }

    fun deleteOtpInDatabase(email: String) {
        this.otpDatabase.deleteById(email)
    }
}