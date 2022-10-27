package com.example.frontend.api

import com.example.frontend.models.LoginDTO
import com.example.frontend.models.RegisterDTO
import retrofit2.Call
import retrofit2.http.*

interface RequestsInterface {
@POST("auth/register")
fun register(@Body user:RegisterDTO): Call<String>
@POST
fun login(@Body user:LoginDTO):Call<String>
}