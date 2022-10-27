package com.example.frontend.api

import com.example.frontend.models.LoginDTO
import com.example.frontend.models.RegisterDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Requests {
    //Svako treba da ima svoj url
    val url: String = "http://192.168.42.232:8080/"

    fun register(email:String,username:String,password:String,passwordConfirm:String){

        var retrofit:Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var requestsInterface : RequestsInterface = retrofit.create(RequestsInterface::class.java)

        var call: Call<String> =
            requestsInterface.register(RegisterDTO(email,username,password,passwordConfirm))

        call.enqueue(object:Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                TODO("Not yet implemented")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    fun login(email:String,password:String){

        var retrofit:Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var requestsInterface : RequestsInterface = retrofit.create(RequestsInterface::class.java)

        var call: Call<String> = requestsInterface.login(LoginDTO(email,password))

        call.enqueue(object:Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                TODO("Not yet implemented")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}