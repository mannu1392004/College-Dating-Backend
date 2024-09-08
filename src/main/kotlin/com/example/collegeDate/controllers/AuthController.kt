package com.example.collegeDate.controllers

import com.example.collegeDate.model.EmailAndOtp
import com.example.collegeDate.model.UsernameAndPassword
import com.example.collegeDate.model.Tokens
import com.example.collegeDate.model.User
import com.example.collegeDate.model.newuser.NewUserData
import com.example.collegeDate.security.jwtutil.JwtUtil
import com.example.collegeDate.service.EmailService
import com.example.collegeDate.service.algo.MatchingAlgo
import com.example.collegeDate.service.database.AuthDatabaseService
import com.example.collegeDate.service.database.DatabaseOperations
import io.jsonwebtoken.JwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.sql.SQLIntegrityConstraintViolationException
import java.util.concurrent.CompletableFuture
import kotlin.random.Random


@RestController
@RequestMapping("/auth")
class AuthController(
    private val jwtUtil: JwtUtil,
    private val matchingAlgo: MatchingAlgo
) {


    @Autowired
    private lateinit var emailService: EmailService

    @Autowired
    private lateinit var databaseService: AuthDatabaseService

    @Autowired
    private lateinit var databaseOperations: DatabaseOperations


    // signup
    @PostMapping("/signup")
    fun signup(
        @RequestBody email: String
    ): String {
        if (!databaseOperations.isEmailPresent(email)) {
            val otp = Random.nextInt(1000, 9999)
            emailService.sendOtp(otp = otp.toString(), to = email)
            databaseService.saveOtpInDatabase(
                email = email,
                otp = otp.toString(),
            )
            return email
        } else {
            return "0"
        }
    }


    // verification -0 = Not Verified
    @PostMapping("/otpVerification")
    fun otpVerify(@RequestBody emailAndOtp: EmailAndOtp): String {
        return if (databaseService.verifyOtpInDatabase(emailAndOtp.email, emailAndOtp.otp)) {
            databaseService.deleteOtpInDatabase(emailAndOtp.email)
            jwtUtil.generateEmailToken(email = emailAndOtp.email)
        } else {
            "0"
        }
    }


    // login
    @PostMapping("/login")
    fun login(@RequestBody usernameAndPassword: UsernameAndPassword): ResponseEntity<Tokens> {
        try {


            if (databaseOperations.isUsernamePresent(usernameAndPassword.username)) {
                if (databaseOperations.verifyPassword(usernameAndPassword.username, usernameAndPassword.password)) {
                    val username = usernameAndPassword.username
                    val tokens = jwtUtil.getTokens(username)
                    databaseOperations.addInvalidToken(tokens.refreshToken)

                    return ResponseEntity.ok(databaseOperations.getUser(username)?.step?.let {
                        Tokens(
                            tokens.accessToken,
                            tokens.refreshToken,
                            "Generated",
                            step = it
                        )
                    })
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Tokens("", "", "Invalid Credentials"))
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Tokens("", "", "Something Went wrong"))
        }
    }

    // logout


    @PostMapping("/refreshToken")
    fun refreshToken(@RequestBody tokens: Tokens): ResponseEntity<Tokens> {


        if (tokens.refreshToken.isNullOrEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Tokens("", "", "bsdk bakchodi mat kar token bhejde"))
        }

        return try {
            if (jwtUtil.validRefreshToken(tokens.refreshToken) && databaseOperations.invalidCheck(tokens.refreshToken)) {
                val username = jwtUtil.extractUserName(tokens.refreshToken)
                val newTokens = jwtUtil.getTokens(username)
                databaseOperations.addInvalidToken(tokens.refreshToken)
                ResponseEntity.ok(Tokens(newTokens.accessToken, newTokens.refreshToken, "Generated"))
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Tokens("", "", "Something Went Wrong"))
            }
        } catch (e: JwtException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Tokens("", "", "Token Expired or Invalid"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Tokens("", "", "Internal Server Error"))
        }
    }


    // creatingDummyUser
    @PostMapping("/createDummyUser")
    fun createDummyUser(@RequestBody newUserData: NewUserData, email: String = Random.nextInt(123).toString()): Tokens {
        val username = newUserData.username
        if (databaseOperations.isUsernamePresent(username)) {
            return Tokens("", "", "Username Already Exist")
        } else {
            val interests = newUserData.interests.toSet()

            try {
                // encode password
                val encoder = BCryptPasswordEncoder()
                // Save the user
                databaseOperations.saveUser(
                    User(
                        username = username,
                        email = email,
                        password = encoder.encode(newUserData.password),
                        firstName = newUserData.firstname,
                        lastName = newUserData.lastname,
                        gender = newUserData.gender,
                        collegeId = 0,
                        dateOfBirth = newUserData.dateOfBirth,
                        profilePictureUrl = newUserData.profilePicture,
                        interests = interests,
                        cardPictureUrl = newUserData.cardPictureUrl,
                        collegeName = ""
                    )
                )

                // Start the matching algorithm asynchronously
                CompletableFuture.runAsync {
                    matchingAlgo.matchingAlgoSignup(username)
                }

                // Generate and return tokens immediately after user save
                val token = jwtUtil.getTokens(username)
                return token

            } catch (e: SQLIntegrityConstraintViolationException) {
                return Tokens("", "", "Email Id Exist already")
            } catch (e: Exception) {
                return Tokens("", "", "Sorry the email you are using exist in database")
            }
        }
    }


}

