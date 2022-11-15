package com.example.frontend.data.repository

import com.example.frontend.data.remote.CheckpointApi
import com.example.frontend.data.remote.dto.LocationDTO
import com.example.frontend.data.remote.dto.LoginDTO
import com.example.frontend.data.remote.dto.PostDTO
import com.example.frontend.data.remote.dto.UserDTO
import com.example.frontend.domain.model.RegisterUser
import com.example.frontend.domain.repository.CheckpointRepository

class CheckpointRepositoryImpl(
    private val api : CheckpointApi
) : CheckpointRepository {

    override suspend fun register(appUser: RegisterUser): String {
        return api.register(appUser)
    }

    override suspend fun login(username: String, password: String): LoginDTO {
        return api.login(username, password)
    }

    override suspend fun searchLocation(token: String, name: String): List<LocationDTO> {
        return api.searchLocation(token, name)
    }

    override suspend fun getAll(token: String): List<LocationDTO> {
        return api.getAll(token)
    }

    override suspend fun getPostsFromLocation(token: String, locationId: Long): List<PostDTO> {
        return api.getPostsFromLocation(token, locationId)
    }

    override suspend fun getMyFollowers(token: String): List<UserDTO> {
        return api.getMyFollowers(token)
    }

    override suspend fun getMyFollowing(token: String): List<UserDTO> {
        return api.getMyFollowing(token)
    }

    override suspend fun getMyFollowersCount(token: String): Int {
        return api.getMyFollowersCount(token)
    }

    override suspend fun getMyFollowingCount(token: String): Int {
        return api.getMyFollowingCount(token)
    }

    override suspend fun getMyPostsCount(token: String): Int {
        return api.getMyPostsCount(token)
    }

    override suspend fun getAllFollowingByUser(userId: Long): List<UserDTO> {
        return api.getAllFollowingByUser(userId)
    }

    override suspend fun getAllFollowersPerUser(userId: Long): List<UserDTO> {
        return api.getAllFollowersPerUser(userId)
    }

    override suspend fun followOrUnfollowUser(token: String, userId: Long): String {
        return api.followOrUnfollowUser(token, userId)
    }

    override suspend fun countAllFollowingByUser(userId: Long): Int {
        return api.countAllFollowingByUser(userId)
    }

    override suspend fun countAllFollowersPerUser(userId: Long): Int {
        return api.countAllFollowersPerUser(userId)
    }

    override suspend fun getFollowingByUsername(userId: Long, username: String): List<UserDTO> {
        return api.getFollowingByUsername(userId, username)
    }

    override suspend fun getFollowersByUsername(userId: Long, username: String): List<UserDTO> {
        return api.getFollowingByUsername(userId, username)
    }

}