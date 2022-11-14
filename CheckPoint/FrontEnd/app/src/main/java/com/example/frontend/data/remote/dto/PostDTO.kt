package com.example.frontend.data.remote.dto

import com.example.frontend.domain.model.Post

data class PostDTO(
    val id:Long,
    val description:String,
    val user:UserDTO,
    val location:LocationDTO
)

fun PostDTO.toPost() : Post{
    return Post(
        id = id,
        description = description,
        user = user.toUser(),
        location = location.toLocation()
    )
}
