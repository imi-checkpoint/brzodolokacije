package com.example.frontend.data.remote.dto

import com.example.frontend.domain.model.Location
import com.example.frontend.domain.model.Photo
import com.example.frontend.domain.model.Post
import com.example.frontend.domain.model.Video

data class PostDTO(
    val appUserId:Long,
    val appUserUsername:String,
    val postId:Long,
    val location:LocationDTO,
    val description:String,
    val numberOfLikes:Int
    //val photos:List<Photo>,
    //val videos:List<Video>
)

fun PostDTO.toPost() : Post{
    return Post(
        postId = postId,
        description = description,
        appUserId = appUserId,
        appUserUsername = appUserUsername,
        numberOfLikes = numberOfLikes,
        //photos = photos,
        //videos = videos,
        location = location.toLocation()
    )
}
