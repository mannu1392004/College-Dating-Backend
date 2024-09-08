package com.example.collegeDate.repository

import com.example.collegeDate.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {

    // Update username by ID
    @Query("{ '_id': ?0 }")
    fun updateUsername(id: String, username: String): User?

    // Find by email
    fun findByEmail(email: String): User?

    // Find by username
    fun findByusername(username: String): User?

    // Find ID by username
    @Query("{ 'username': ?0 }", fields = "{ '_id': 1 }")
    fun findIdByUsername(username: String): String?

    //find by college name
    fun findBycollegeId(collegeId: Int): List<User>?

    //find by gender
    fun findByGender(gender: Int): List<User>?


    // sortBy both gender and collegeName
    fun findBycollegeIdAndGender(collegeId: Int, gender: Int): List<User>?

}

