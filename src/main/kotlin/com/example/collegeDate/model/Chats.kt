package com.example.collegeDate.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.UUID

@Document(collection = "chats") // Specify the collection name in MongoDB
data class Chats(
    @Id
    val chatId: UUID = UUID.randomUUID(),

    @Field("messages")
    var messages: MutableList<UUID> = mutableListOf(),

    // new messages
    @Field("new_messages")
    var newMessages: MutableList<UUID> = mutableListOf()
)