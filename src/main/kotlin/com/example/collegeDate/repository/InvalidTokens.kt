package com.example.collegeDate.repository

import com.example.collegeDate.model.EmailInvalidToken

import org.springframework.data.mongodb.repository.MongoRepository

interface InvalidTokens :MongoRepository<EmailInvalidToken,String>{
}