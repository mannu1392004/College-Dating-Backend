package com.example.collegeDate.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.UUID

@Document(collection = "messages") // Specify the collection name in MongoDB
data class Messages(
    @Id
    val id: UUID = UUID.randomUUID(),  // Unique identifier for the message, typically a String in MongoDB

    @Field("image_or_text")
    var imageOrText: Boolean,

    @Field("text")
    var text: String = "",

    @Field("time")
    var time: String = "",

    @Field("sender")
    var sender: String,

    @Field("receiver")
    var receiver: String,

    @Field("is_seen")
    var isSeen: Boolean,

    @Field("seen_at")
    var seenAt: String = ""
)
