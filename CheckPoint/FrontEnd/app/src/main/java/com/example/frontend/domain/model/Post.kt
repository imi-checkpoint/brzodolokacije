package com.example.frontend.domain.model

data class Post(
    val id:Long,
    val description:String,
    val user:User,
    val location: Location
)
