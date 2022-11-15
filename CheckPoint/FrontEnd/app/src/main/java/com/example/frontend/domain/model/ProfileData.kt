package com.example.frontend.domain.model

data class ProfileData (
    val followers : List<User>,
    val following : List<User>,
    val followersCount : Int,
    val followingCount : Int,
    val postCount : Int
)