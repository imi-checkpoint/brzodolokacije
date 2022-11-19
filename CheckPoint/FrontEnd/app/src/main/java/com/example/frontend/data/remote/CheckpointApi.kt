package com.example.frontend.data.remote

import com.example.frontend.data.remote.dto.*
import com.example.frontend.domain.model.RegisterUser
import retrofit2.http.*

interface CheckpointApi {
    @POST("api/register")
    suspend fun register(
        @Body appUser:RegisterUser
    ): String

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("username") username:String,
        @Field("password") password:String
    ) : LoginDTO

    @Headers("Content-Type: application/json")
    @GET("location/all")
    suspend fun getAll(
        @Header("Authorization") token : String
    ): List<LocationDTO>

    @Headers("Content-Type: application/json")
    @GET("location/keyword/{name}")
    suspend fun searchLocation(
        @Header("Authorization") token : String,
        @Path("name") name:String
    ): List<LocationDTO>

    @Headers("Content-Type: application/json")
    @GET("post/location/{locationId}")
    suspend fun getPostsFromLocation(
        @Header("Authorization") token : String,
        @Path("locationId") locationId:Long
    ): List<PostDTO>

    @Headers("Content-Type: application/json")
    @GET("post/user/{userId}")
    suspend fun getPostsByUserId(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long
    ): List<PostDTO>

    @Headers("Content-Type: application/json")
    @GET("follow_list/my/following")
    suspend fun getMyFollowing(
        @Header("Authorization") token : String
    ) : List<UserDTO>

    @Headers("Content-Type: application/json")
    @GET("follow_list/my/followers")
    suspend fun getMyFollowers(
        @Header("Authorization") token : String
    ) : List<UserDTO>

    @Headers("Content-Type: application/json")
    @GET("follow_list/my/following/count")
    suspend fun getMyFollowingCount(
        @Header("Authorization") token : String
    ) : Int

    @Headers("Content-Type: application/json")
    @GET("follow_list/my/followers/count")
    suspend fun  getMyFollowersCount(
        @Header("Authorization") token : String
    ) : Int

    @Headers("Content-Type: application/json")
    @GET("post/my/count")
    suspend fun getMyPostsCount(
        @Header("Authorization") token : String
    ) : Int

    @Headers("Content-Type: application/json")
    @DELETE("post/delete/{id}")
    suspend fun detelePostById(
        @Header("Authorization") token: String,
        @Path("id") postId: Long
    ): String

    ///////////////////////////

    @Headers("Content-Type: application/json")
    @GET("follow_list/{userId}/following")
    suspend fun getAllFollowingByUser(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long
    ) : List<UserDTO>

    @Headers("Content-Type: application/json")
    @GET("follow_list/{userId}/followers")
    suspend fun getAllFollowersPerUser(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long
    ) : List<UserDTO>

    @Headers("Content-Type: application/json")
    @POST("follow_list/follow_or_unfollow/{userId}")
    suspend fun followOrUnfollowUser(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long
    ) : String

    @Headers("Content-Type: application/json")
    @GET("follow_list/{userId}/following/count")
    suspend fun countAllFollowingByUser(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long
    ) : Int

    @Headers("Content-Type: application/json")
    @GET("follow_list/{userId}/followers/count")
    suspend fun countAllFollowersPerUser(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long
    ) : Int

    @Headers("Content-Type: application/json")
    @GET("post/user/{userId}/count")
    suspend fun getUserPostsCount(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long
    ) : Int

    @Headers("Content-Type: application/json")
    @GET("follow_list/{userId}/following/keyword/{username}")
    suspend fun getFollowingByUsername(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long,
        @Path("username") username : String
    ) : List<UserDTO>

    @Headers("Content-Type: application/json")
    @GET("follow_list/{userId}/followers/keyword/{username}")
    suspend fun getFollowersByUsername(
        @Header("Authorization") token : String,
        @Path("userId") userId:Long,
        @Path("username") username : String
    ) : List<UserDTO>

    @Headers("Content-Type: application/json")
    @GET("follow_list/my/following/keyword/{username}")
    suspend fun getMyFollowingByUsername(
        @Header("Authorization") token : String,
        @Path("username") username : String
    ) : List<UserDTO>

    @Headers("Content-Type: application/json")
    @GET("follow_list/my/followers/keyword/{username}")
    suspend fun getMyFollowersByUsername(
        @Header("Authorization") token : String,
        @Path("username") username : String
    ) : List<UserDTO>

    @Headers("Content-Type: application/octet-stream")
    @GET("photos/photoByPostIdAndOrder/{postId}/{order}")
    suspend fun GetPhotoByPostIdAndOrder(
        @Header("Authorization") token : String,
        @Path("postId") postId: Long,
        @Path("order") order: Int
    ) : BinaryPhoto


}