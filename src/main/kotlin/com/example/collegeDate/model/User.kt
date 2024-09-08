package com.example.collegeDate.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*


@Document(collection = "users")
@CompoundIndexes(
    CompoundIndex(name = "college_gender_idx", def = "{'collegeName': 1, 'gender': 1}")
)
data class User(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Field("step")
    var step: Int = 0,

    @Field("username")
    var username: String,

    @Field("email")
    var email: String,

    @Field("password")
    var password: String,

    @Field("first_name")
    var firstName: String,

    @Field("last_name")
    var lastName: String,

    @Field("interests")
    var interests: Set<String> = setOf(),

    // 0 for male 1 for female
    @Field("gender")
    var gender: Int,

    @Field("date_of_birth")
    var dateOfBirth: String,

    @Field("bio")
    var bio: String? = "",

    @Field("profile_picture_url")
    var profilePictureUrl: String? = "",

    @Field("card_picture_url")
    var cardPictureUrl: String? ="",

    @Field("requests_sent")
    var requestsSent: MutableList<String>? = mutableListOf(),

    @Field("requests")
    var requests: MutableList<String>? = mutableListOf(),

    @Field("college_Id")
    var collegeId: Int,

    @Field("college_name")
    var collegeName: String,

    @Field("matched")
    var matched: List<String>? = emptyList(),

    @Field("profile_access")
    var profileAccess: MutableList<String> = mutableListOf(),

    @Field("posts")
    var posts: MutableList<Posts>? = mutableListOf(),

    @Field("chats")
    var chats: MutableList<UUID>? = mutableListOf()

)