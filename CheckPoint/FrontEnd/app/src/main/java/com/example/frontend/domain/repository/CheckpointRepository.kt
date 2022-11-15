package com.example.frontend.domain.repository

import com.example.frontend.data.remote.dto.LocationDTO
import com.example.frontend.data.remote.dto.LoginDTO
import com.example.frontend.data.remote.dto.PostDTO
import com.example.frontend.data.remote.dto.UserDTO
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
}