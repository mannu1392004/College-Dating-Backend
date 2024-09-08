package com.example.collegeDate.controllers

import com.example.collegeDate.data.CollegeData
import com.example.collegeDate.model.CollegeList
import com.example.collegeDate.model.interests.Interests
import com.example.collegeDate.model.newuser.BasicProfileDetail
import com.example.collegeDate.service.aws.S3Service
import com.example.collegeDate.service.database.DatabaseOperations
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/public")
class Public {
    @Autowired
    lateinit var s3Service: S3Service

    @Autowired
    lateinit var collegeData: com.example.collegeDate.repository.CollegeData

    @Autowired
    lateinit var databaseOperations: DatabaseOperations

    @GetMapping("/getInterests")
    fun sendInterestsList(): List<Interests> {
        val interestsList = listOf(
            Interests("Programming", 1),
            Interests("Web Development", 2),
            Interests("Mobile App Development", 3),
            Interests("Artificial Intelligence & Machine Learning", 4),
            Interests("Cybersecurity & Ethical Hacking", 5),
            Interests("Blockchain Technology", 6),
            Interests("Data Science & Analytics", 7),
            Interests("Cloud Computing", 8),
            Interests("Gaming", 9),
            Interests("AR/VR (Augmented & Virtual Reality)", 10),
            Interests("UI/UX Design", 11),
            Interests("Open Source Contributions", 12),
            Interests("Electronics & Robotics", 13),
            Interests("IoT (Internet of Things)", 14),
            Interests("DevOps & Automation", 15),
            Interests("Cryptocurrency & NFTs", 16),
            Interests("Software Testing & Quality Assurance", 17),
            Interests("Quantum Computing", 18),
            Interests("Movies & TV Shows", 19),
            Interests("Music", 20),
            Interests("Photography & Videography", 21),
            Interests("Traveling & Backpacking", 22),
            Interests("Fitness & Gym", 23),
            Interests("Cricket & Sports", 24),
            Interests("Reading & Writing", 25),
            Interests("Food & Cooking", 26),
            Interests("Startups & Entrepreneurship", 27),
            Interests("Fashion & Style", 28),
            Interests("Art & Painting", 29),
            Interests("Social Media Influencing", 30),
            Interests("Public Speaking & Debating", 31),
            Interests("Volunteering & Social Work", 32),
            Interests("Pet Lovers", 33),
            Interests("Dancing", 34),
            Interests("Anime & Manga", 35),
            Interests("E-Sports", 36),
            Interests("Comedy & Stand-up Shows", 37),
            Interests("Languages & Cultural Studies", 38),
            Interests("Meditation & Yoga", 39),
            Interests("Environmental Activism", 40),
            Interests("Gardening & Plant Care", 41),
            Interests("Travel Blogging", 42),
            Interests("Street Photography", 43),
            Interests("DIY Projects & Crafting", 44),
            Interests("Interior Design", 45),
            Interests("Veganism & Vegetarianism", 46),
            Interests("Home Brewing & Mixology", 47),
            Interests("Astrology & Tarot Reading", 48),
            Interests("Self-Improvement & Productivity", 49),
            Interests("Board Games & Puzzles", 50),
            Interests("Chess & Strategy Games", 51),
            Interests("Podcasts & Audiobooks", 52),
            Interests("Mental Health Awareness", 53),
            Interests("Biking & Cycling", 54),
            Interests("Adventure Sports", 55),
            Interests("Historical Reenactments", 56),
            Interests("Cultural Festivals & Events", 57),
            Interests("Technology Gadgets", 58),
            Interests("Wildlife & Bird Watching", 59),
            Interests("Online Communities & Forums", 60)
        )


        return interestsList


    }

    @PostMapping("/checkUserValid")
    fun checkUserValid(
        @RequestBody username: String
    ):
            Boolean {
        return databaseOperations.isUsernamePresent(username)
    }



    @GetMapping("/colleges")
    fun getCollegeSuggestions(name: String): List<CollegeList> {
        return collegeData.findByCollegeNameContainingIgnoreCase(name)
    }


    // delete
    @GetMapping("/user/{username}/{all}")
    fun delete(
        @PathVariable  username: String,
        @PathVariable  all:String
    ){
        if (all=="0"){
            databaseOperations.deleteUser(username)
        }
        if (all=="1"){
            databaseOperations.deleteUser(username)
        }
    }


    @GetMapping("/getData")
    fun getData(
    ):List<com.example.collegeDate.model.User>{
        return databaseOperations.getAllData()
    }





}