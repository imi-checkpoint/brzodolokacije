package com.example.frontend.domain.model

data class Photo(
    val id:String,
    val order:Int,
    val postId:Long,
    val photo:ByteArray
)
