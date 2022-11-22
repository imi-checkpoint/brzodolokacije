package com.example.frontend.domain.model

data class Post(
    val postId:Long,
    val description:String,
    val appUserId:Long,
    val appUserUsername:String,
    val location: Location,
    val numberOfLikes:Int,
    val numberOfComments:Int,
    val isLiked:Boolean,
    val photos:List<Photo>,
    //val videos:List<Video>
)
