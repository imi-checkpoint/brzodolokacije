package com.example.frontend.data.repository

import com.example.frontend.data.remote.CheckpointApi
import com.example.frontend.data.remote.dto.*
import com.example.frontend.domain.model.RegisterUser
import com.example.frontend.domain.repository.CheckpointRepository
import okhttp3.MultipartBody

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

    override suspend fun getPostsByUserId(token: String, userId: Long): List<PostDTO> {
        return api.getPostsByUserId(token, userId)
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

    ////

    override suspend fun getAllFollowingByUser(token: String, userId: Long): List<UserDTO> {
        return api.getAllFollowingByUser(token,userId)
    }

    override suspend fun getAllFollowersPerUser(token: String, userId: Long): List<UserDTO> {
        return api.getAllFollowersPerUser(token,userId)
    }

    override suspend fun followOrUnfollowUser(token: String, userId: Long): String {
        return api.followOrUnfollowUser(token, userId)
    }

    override suspend fun countAllFollowingByUser(token: String, userId: Long): Int {
        return api.countAllFollowingByUser(token,userId)
    }

    override suspend fun countAllFollowersPerUser(token: String, userId: Long): Int {
        return api.countAllFollowersPerUser(token,userId)
    }

    override suspend fun getUserPostsCount(token: String, userId: Long): Int {
        return api.getUserPostsCount(token, userId);
    }

    override suspend fun getFollowingByUsername(token: String, userId: Long, username: String): List<UserDTO> {
        return api.getFollowingByUsername(token,userId, username)
    }

    override suspend fun getFollowersByUsername(token: String, userId: Long, username: String): List<UserDTO> {
        return api.getFollowersByUsername(token,userId, username)
    }

    override suspend fun getMyFollowersByUsername(
        token: String,
        username: String
    ): List<UserDTO> {
        return api.getMyFollowersByUsername(token, username);
    }

    override suspend fun getMyFollowingByUsername(
        token: String,
        username: String
    ): List<UserDTO> {
        return api.getMyFollowingByUsername(token, username);
    }

    override suspend fun getPhotoByPostIdAndOrder(
        token: String,
        postId: Long,
        order: Int
    ): BinaryPhoto {
        return api.GetPhotoByPostIdAndOrder(token, postId, order)
    }

    override suspend fun deletePostById(token: String, postId: Long): String {
        return api.detelePostById(token, postId)
    }

    override suspend fun getUserId(token: String): Long {
        return api.getUserId(token)
    }

    override suspend fun getPostById(token: String, postId: Long): PostDTO {
        return api.getPostById(token, postId)
    }

    override suspend fun savePost(token: String,description:String,locationId:Long): Long{
        return api.savePost(token,description,locationId)
    }

    override suspend fun addImage(token: String, postId: Long, order: Int, photo: MultipartBody.Part) : String {
        return api.addImage(token,postId,order,photo)
    }

    override suspend fun getNumberOfLikesByPostId(token: String, postId: Long): Int {
        return api.getNumberOfLikesByPostId(token, postId);
    }

    override suspend fun getNumberOfCommentsByPostId(token: String, postId: Long): Int {
        return api.getNumberOfCommentsByPostId(token, postId);
    }

    override suspend fun likeOrUnlikePostById(token: String, postId: Long): String {
        return api.likeOrUnlikePostById(token, postId);
    }


}