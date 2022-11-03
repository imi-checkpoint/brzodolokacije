package com.example.frontend.api

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.frontend.Constants
import com.example.frontend.activities.LoginActivity
import com.example.frontend.activities.SearchLocationActivity
import com.example.frontend.models.LocationDTO
import com.example.frontend.models.RegisterDTO
import com.example.frontend.models.Tokens
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class Requests {
    companion object{


    var token : Tokens? = null;

    fun register(email:String, username:String, password:String, passwordConfirm:String, context : Context){

        if(password != passwordConfirm){
            Toast.makeText(
                context,
                "Passwords don't match!",
                Toast.LENGTH_LONG
            ).show();
            return ;
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var requestsInterface : RequestsInterface = retrofit.create(RequestsInterface::class.java)

        var call: Call<String> =
            requestsInterface.register(RegisterDTO(email,username,password));

        val nes = this
        call.enqueue(object:Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                //Log.d("REQUESTS REGISTER" , "User registered success");
                val intent = Intent(context, LoginActivity::class.java);
                context.startActivity(intent);
                Toast.makeText(
                    context,
                    "Register successfull",
                    Toast.LENGTH_SHORT
                ).show();
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("REQUESTS REGISTER" , "User register error!");
                Log.d("RE", t.toString());
            }
        })
    }

    fun login(username:String,password:String, context: Context){
        //Log.d("sss", username+" "+password);

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var requestsInterface : RequestsInterface = retrofit.create(RequestsInterface::class.java)

        var call: Call<Tokens> = requestsInterface.login(username,password);

        val nes = this
        call.enqueue(object:Callback<Tokens>{
            override fun onResponse(call: Call<Tokens>, response: Response<Tokens>) {
                //Log.d("REQUESTS LOGIN" , "User logged in success");
                token = response.body()!!;
                Log.d("LOGIN CREDS", "token "+ token!!.access_token+" refresh "+ token!!.refresh_token);

                //predji na search
                val intent = Intent(context, SearchLocationActivity::class.java);
                context.startActivity(intent);

                Toast.makeText(
                    context,
                    "Login successfull",
                    Toast.LENGTH_SHORT
                ).show();
            }
            override fun onFailure(call: Call<Tokens>, t: Throwable) {
                Log.d("REQUESTS LOGIN" , "User login error!");
                Log.d("RE", t.toString());
                Toast.makeText(
                    context,
                    "Wrong credentials!",
                    Toast.LENGTH_SHORT
                ).show();
            }
        })
    }

        fun search(searchText: String): List<LocationDTO> {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            var requestsInterface : RequestsInterface = retrofit.create(RequestsInterface::class.java)

            val call: Call<List<LocationDTO>> = requestsInterface.searchLocation(searchText);

            var lista:List<LocationDTO> = emptyList()
            call.enqueue(object:Callback<List<LocationDTO>>{
                override fun onResponse(call: Call<List<LocationDTO>>, response: Response<List<LocationDTO>>) {
                    lista = response.body()!!
                    println("Test123")
                }
                override fun onFailure(call: Call<List<LocationDTO>>, t: Throwable){
                    println(t.message)
                }
            })
            return lista
        }
    }
}