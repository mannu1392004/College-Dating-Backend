package com.example.collegeDate.service.database

import com.example.collegeDate.model.EmailInvalidToken
import com.example.collegeDate.model.User
import com.example.collegeDate.repository.InvalidTokens
import com.example.collegeDate.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class DatabaseOperations @Autowired constructor(
    private val userRepository: UserRepository,
    private val invalidToken: InvalidTokens
) {

    // check if email exist in the database
    fun isEmailPresent(
        email: String
    ): Boolean {
        return userRepository.findByEmail(email) != null
    }


    // checking invalid token
    fun invalidCheck(
        token: String
    ): Boolean {
        return invalidToken.findByIdOrNull(token) == null
    }

    // adding invalid token
    fun addInvalidToken(
        token: String
    ) {
        invalidToken.save(EmailInvalidToken(token))
    }


    // find by id
    fun getUserById(
        id: String
    ): User? {
        return userRepository.findByIdOrNull(id)
    }


    // checking if username present in database
    fun isUsernamePresent(
        username: String
    ): Boolean {
        return userRepository.findByusername(username) != null
    }

    // saving user in the database
    fun saveUser(
        user: User
    ) {
        userRepository.save(user)
    }

    // get the user from the database
    fun getUser(
        username: String
    ): User? {
        return userRepository.findByusername(username)
    }

    // get all users
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    // clear the matching list
    fun clearMatchList(userId: String) {
        val user = getUserById(userId)
        user?.matched = emptyList()
        if (user != null) {
            saveUser(user)
        }
    }


    fun saveMatchingList(matchingId: String, userId: String) {
        val user = getUserById(userId)
        user?.matched = user?.matched?.plus(matchingId)
        if (user != null) {
            saveUser(user)
        }
    }

    // verify username and password
    fun verifyPassword(
        username: String, password: String
    ): Boolean {
        val user = getUser(username) ?: return false
        val decoder = BCryptPasswordEncoder()
        return user.password == decoder.encode(password)
    }


    // sort by collegeName
    fun sortByCollegeName(
        collegeName: Int
    ): List<User> {
        return userRepository.findBycollegeId(collegeName) ?: emptyList()
    }

    // sort by gender
    fun sortByGender(gender: Int): List<User> {
        return userRepository.findByGender(gender) ?: emptyList()
    }

    // sort by both
    fun sortByBoth(collegeName: Int, gender: Int): List<User> {
        return userRepository.findBycollegeIdAndGender(collegeName, gender) ?: emptyList()
    }

    // delete user
    fun deleteUser(
        userId: String
    ) {
        val user = getUser(userId)
        user?.id?.let { userRepository.deleteById(it) }
    }

    // delete all user
    fun deleteAllUser() {
        userRepository.deleteAll()
    }

    // get all the data
    fun getAllData(): List<User> {
        return userRepository.findAll()
    }

}