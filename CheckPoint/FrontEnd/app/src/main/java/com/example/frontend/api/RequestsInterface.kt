package com.example.frontend.api

import com.example.frontend.models.RegisterDTO
import com.example.frontend.models.Tokens
import retrofit2.Call
import retrofit2.http.*

interface RequestsInterface {
    @POST("api/register")
    fun register(
        @Body appUser:RegisterDTO
    ):Call<String>

    @FormUrlEncoded
    @POST("api/login")
    fun login(@Field("username") username:String, @Field("password") password:String):Call<Tokens>

}