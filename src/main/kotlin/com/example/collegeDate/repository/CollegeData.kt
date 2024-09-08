package com.example.collegeDate.repository

import com.example.collegeDate.model.CollegeList
import org.springframework.data.mongodb.repository.MongoRepository

interface CollegeData:MongoRepository<CollegeList,Int> {
    fun findByCollegeNameContainingIgnoreCase(name: String): List<CollegeList>
}