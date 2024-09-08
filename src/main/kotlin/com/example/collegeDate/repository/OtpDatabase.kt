package com.example.collegeDate.repository

import com.example.collegeDate.model.OtpSavedInDatabase

import org.springframework.data.mongodb.repository.MongoRepository

interface OtpDatabase :MongoRepository<OtpSavedInDatabase,String>