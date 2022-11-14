package com.example.frontend.data.remote

import com.example.frontend.data.remote.dto.LocationDTO
import com.example.frontend.data.remote.dto.LoginDTO
import com.example.frontend.data.remote.dto.PostDTO
import com.example.frontend.data.remote.dto.UserDTO
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

}