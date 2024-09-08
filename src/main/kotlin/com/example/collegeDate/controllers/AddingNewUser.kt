package com.example.collegeDate.controllers

import com.example.collegeDate.model.Tokens
import com.example.collegeDate.model.User
import com.example.collegeDate.model.newuser.BasicProfileDetail
import com.example.collegeDate.model.newuser.Interests
import com.example.collegeDate.model.newuser.newUsername
import com.example.collegeDate.security.jwtutil.JwtUtil
import com.example.collegeDate.service.algo.MatchingAlgo
import com.example.collegeDate.service.aws.S3Service
import com.example.collegeDate.service.database.DatabaseOperations
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.SQLIntegrityConstraintViolationException
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/newUser")
class AddingNewUser(
    private val jwtUtil: JwtUtil,
    private val databaseOperations: DatabaseOperations,
    private val matchingAlgo: MatchingAlgo,
    private val s3Service: S3Service
) {
    // adding the username
    @PostMapping("/createNewUser")
    fun createNewUser(
        authentication: Authentication,
        @RequestBody newUserName: newUsername
    ): Tokens {
        val email = authentication.name
        val username = newUserName

        if (databaseOperations.isUsernamePresent(username.username)) {
            return Tokens("", "", "Username Already Exist")
        } else {
            try {
                // encode password
                val encoder = BCryptPasswordEncoder()
                // Save the user
                databaseOperations.saveUser(
                    User(
                        username = username.username,
                        email = email,
                        password = encoder.encode(newUserName.password),
                        firstName = "Not Filled Yet",
                        lastName = "",
                        gender = 3,
                        collegeId = 0,
                        dateOfBirth = "Pending",
                        profilePictureUrl = "",
                        interests = emptySet(),
                        cardPictureUrl = "",
                        collegeName = ""
                    )
                )

                // Start the matching algorithm asynchronously
                //CompletableFuture.runAsync {
                //   matchingAlgo.matchingAlgoSignup(username)
                //}

                // Generate and return tokens immediately after user save
                val token = jwtUtil.getTokens(username.username)
                return token

            } catch (e: SQLIntegrityConstraintViolationException) {
                return Tokens("", "", "Email Id Exist already")
            } catch (e: Exception) {
                return Tokens("", "", "Sorry the email you are using exist in database")
            }
        }
    }

    // basic profile details
    @PostMapping("/addBasicDetails")
    fun addBasicProfileDetails(
        authentication: Authentication,
        @RequestParam("image") image: MultipartFile,
        @RequestParam("details") details: String
    ): ResponseEntity<String> {
        val username = authentication.name
        val user = databaseOperations.getUser(username) ?: return ResponseEntity(
            "User not found",
            HttpStatus.NOT_FOUND
        )

        if (user.step == 0) {
            return try {

                val fileName =
                    image.originalFilename ?: return ResponseEntity("Invalid file name", HttpStatus.BAD_REQUEST)
                val basicProfileDetail = parseDetails(details)

                // Validate the image type
                if (!isImageFile(image)) {
                    return ResponseEntity("Invalid file type. Please upload an image.", HttpStatus.BAD_REQUEST)
                }
                // Upload image to S3 directly from the input stream
                val s3Url = s3Service.uploadFileToS3(image.inputStream, fileName)

                user.firstName = basicProfileDetail.firstName
                user.lastName = basicProfileDetail.lastName
                user.collegeId = basicProfileDetail.collegeId
                user.profilePictureUrl = s3Url
                user.dateOfBirth = basicProfileDetail.dob
                user.collegeName = basicProfileDetail.collegeName
                user.step = 1
                databaseOperations.saveUser(user)

                ResponseEntity("0", HttpStatus.OK)
            } catch (e: Exception) {
                ResponseEntity("Failed to upload image: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
            }
        } else {
            return ResponseEntity("Fuck Off Baby🖕", HttpStatus.BAD_REQUEST)
        }

    }

    // adding the gender
    @PostMapping("/addGender")
    fun addGender(
        @RequestBody gender: String,
        authentication: Authentication,

        ): String {
        val userName = authentication.name
        val user = databaseOperations.getUser(userName) ?: return "User Not Found"
        if (user.step == 1) {
            user.gender = gender.toInt()
            user.step = 2
            databaseOperations.saveUser(user)
            return "0"
        } else {
            return "No Baby Noooooo Bad request mat dalo"
        }

    }

    //adding interests
    @PostMapping("/addInterests")
    fun addInterests(
        @RequestBody list: Interests,
        authentication: Authentication
    ): String {
        val username = authentication.name
        val user = databaseOperations.getUser(username) ?: return "User Not Found"
        return if (user.step == 2) {
            user.interests = list.interests.toSet()
            user.step = 3
            databaseOperations.saveUser(user)

            //Start the matching algorithm asynchronously
            CompletableFuture.runAsync {
                matchingAlgo.matchingAlgoSignup(username)
            }
            "0"
        } else {
            "No Baby Noooooo Bad request mat dalo"
        }
    }

    // card picture url
    @PostMapping("/addCardPicture")
    fun addCardPicture(
        @RequestBody image: MultipartFile,
        authentication: Authentication
    ): String {
        val username = authentication.name
        val user = databaseOperations.getUser(username) ?: return "User Not Found"
        if (user.step == 3) {
            if (!isImageFile(image)) {
                return "Invalid Image"
            }
            val s3Url = s3Service.uploadFileToS3(image.inputStream, "")
            user.cardPictureUrl = s3Url
            user.step = 4
            databaseOperations.saveUser(user)

            return "0"


        } else {
            return "No Baby Noooooo Bad request mat dalo"
        }


    }


    private fun isImageFile(file: MultipartFile): Boolean {
        val mimeType = file.contentType ?: return false
        return mimeType.startsWith("image/")
    }

    private fun parseDetails(details: String): BasicProfileDetail {
        return jacksonObjectMapper().readValue(details, BasicProfileDetail::class.java)
    }




}