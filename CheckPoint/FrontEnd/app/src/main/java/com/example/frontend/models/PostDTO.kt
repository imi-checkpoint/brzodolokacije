package com.example.frontend.models

import java.time.LocalDateTime

data class PostDTO(
    val id:Long,
    val description:String,
    val user:UserDTO,
    val location:LocationDTO
)
