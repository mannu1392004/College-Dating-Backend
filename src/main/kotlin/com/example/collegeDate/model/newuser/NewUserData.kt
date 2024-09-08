package com.example.collegeDate.model.newuser

data class NewUserData(
    val username:String,
    val  firstname: String,
    val lastname:String,
    val  password: String,
    val  gender: Int,
    val  dateOfBirth: String,
    val  interests: List<String>,
    val  about: String,
    val  profilePicture: String?,
    val collegeName:String,
    val cardPictureUrl:String,
)
