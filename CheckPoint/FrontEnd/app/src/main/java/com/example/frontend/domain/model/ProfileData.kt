package com.example.frontend.domain.model

data class ProfileData (
    val followersCount : Int,
    val followingCount : Int,
    val postCount : Int,
    val amFollowing : Boolean
)