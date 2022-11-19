package com.example.frontend.domain.repository

import com.example.frontend.data.remote.dto.*
import com.example.frontend.domain.model.RegisterUser

interface CheckpointRepository {
    suspend fun register(appUser : RegisterUser) : String

    suspend fun login(username : String, password : String) : LoginDTO

    suspend fun searchLocation(token : String, name : String) : List<LocationDTO>

    suspend fun getAll(token: String) : List<LocationDTO>

    suspend fun getPostsFromLocation(token : String, locationId : Long) : List<PostDTO>

    suspend fun getMyFollowers(token: String): List<UserDTO>

    suspend fun getMyFollowing(token: String): List<UserDTO>

    suspend fun getMyFollowersCount(token: String): Int

    suspend fun getMyFollowingCount(token: String): Int

    suspend fun getMyPostsCount(token: String): Int

    suspend fun getAllFollowingByUser(token: String, userId : Long): List<UserDTO>

    suspend fun getAllFollowersPerUser(token: String, userId : Long): List<UserDTO>

    suspend fun followOrUnfollowUser(token: String, userId: Long): String

    suspend fun countAllFollowingByUser(token: String, userId : Long): Int

    suspend fun countAllFollowersPerUser(token: String, userId : Long): Int

    suspend fun getUserPostsCount(token: String, userId : Long): Int

    suspend fun getFollowingByUsername(token: String, userId: Long, username: String): List<UserDTO>

    suspend fun getFollowersByUsername(token: String, userId: Long, username: String): List<UserDTO>

    suspend fun getMyFollowersByUsername(token: String, username: String): List<UserDTO>

    suspend fun getMyFollowingByUsername(token: String, username: String): List<UserDTO>

    suspend fun getPhotoByPostIdAndOrder(token: String, postid: Long, order: Int): BinaryPhoto

    suspend fun deletePostById(token : String, postId: Long) : String
}