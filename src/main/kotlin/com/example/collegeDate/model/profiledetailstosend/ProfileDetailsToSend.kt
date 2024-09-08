package com.example.collegeDate.model.profiledetailstosend

import com.example.collegeDate.model.Posts

data class ProfileDetailsToSend(
    val cardPictureUrl:String?=null,
    val username:String?=null,
    val collegeName:String?=null,
    val bio:String?=null,
    val interests:List<String>?=null,
    val commonInterests:List<String>?=null,
    val postImageUrl: MutableList<Posts>? =null,
    val profileAccess:Boolean?=null,
    val requestSent:Boolean?=null,
    val error:String
    )
