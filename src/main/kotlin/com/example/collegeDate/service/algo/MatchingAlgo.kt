package com.example.collegeDate.service.algo

import com.example.collegeDate.service.database.DatabaseOperations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MatchingAlgo {

    @Autowired
    private lateinit var databaseOperations: DatabaseOperations

    @Async
    fun matchingAlgoSignup(username: String) {
        println("hello")

        // get the user with interests initialized
        val user = databaseOperations.getUser(username)


        // user preferences
        val userPreferences = user?.interests ?: emptySet()

        // get all the users with interests initialized
        val allUsers = databaseOperations.getAllUsers()

        allUsers.forEach { otherUser ->





            if (user?.username != otherUser.username&&otherUser.step==4) {
                val otherUserPreferences = otherUser.interests
                val intersection = otherUserPreferences.intersect(userPreferences).size
                val union = otherUserPreferences.union(userPreferences).size

                if (union > 0 && (intersection.toDouble() / union.toDouble() >= 0.4)

                ) {


                    // add to both of the database

                    if (user != null) {

                        user.matched = user.matched?.plus(otherUser.username)

                        otherUser.matched = otherUser.matched?.plus(user.username)

                        println(user.matched)
                        println(otherUser.matched)
                        databaseOperations.saveUser(user)
                        databaseOperations.saveUser(otherUser)
                    }
                }
            }
        }
    }
}
