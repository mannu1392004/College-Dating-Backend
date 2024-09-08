package com.example.collegeDate.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "posts") // Specify the collection name in MongoDB
data class Posts(
    @Id
    val id: String? = null,  // Unique identifier for the post (usually a String in MongoDB)


    @Field("user_name")
    val userName: String,

    @Field("post_image")
    val postImage: String,

    @Field("post_text")
    val postText: String,

    @Field("date_time")
    val dateTime: String
)
