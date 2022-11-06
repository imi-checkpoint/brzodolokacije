package com.example.frontend.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import com.example.frontend.Constants
import com.example.frontend.Screen
import com.example.frontend.models.LocationDTO
import com.example.frontend.models.RegisterDTO
import com.example.frontend.models.Tokens
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

interface CustomCallback {
    fun onSucess(value: List<LocationDTO>)
    fun onFailure()
}

class Requests {
    companion object {

        var token: Tokens? = null;
        var requestsInterface: RequestsInterface = RetrofitInstance.api;

        fun register(
            email: String,
            username: String,
            password: String,
            passwordConfirm: String,
            navController: NavController,
            context: Context
        ) {

            if (password != passwordConfirm) {
                Toast.makeText(
                    context,
                    "Passwords don't match!",
                    Toast.LENGTH_LONG
                ).show();
                return;
            }


            var call: Call<String> =
                requestsInterface.register(RegisterDTO(email, username, password));

            val nes = this
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //Log.d("REQUESTS REGISTER" , "User registered success");
                    navController.navigate(Screen.LoginScreen.route);

                    Toast.makeText(
                        context,
                        "Register successfull",
                        Toast.LENGTH_SHORT
                    ).show();
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("REQUESTS REGISTER", "User register error!");
                    Log.d("RE", t.toString());
                }
            })
        }

        fun login(
            username: String,
            password: String,
            navController: NavController,
            context: Context
        ) {

            var call: Call<Tokens> = requestsInterface.login(username, password);

            val nes = this
            call.enqueue(object : Callback<Tokens> {
                override fun onResponse(call: Call<Tokens>, response: Response<Tokens>) {
                    //Log.d("REQUESTS LOGIN" , "User logged in success");
                    token = response.body()!!;

                    //sacuvaj token
                    GlobalScope.launch(Dispatchers.IO) {
                        DataStoreManager.saveValue(context, "access_token", token!!.access_token);
                        DataStoreManager.saveValue(context, "refresh_token", token!!.refresh_token);
                    }


                    Log.d(
                        "LOGIN CREDS",
                        "token " + token!!.access_token + " refresh " + token!!.refresh_token
                    );

                    //predji na search
                    navController.navigate(Screen.MainSearchScreen.route);

                    Toast.makeText(
                        context,
                        "Login successfull",
                        Toast.LENGTH_SHORT
                    ).show();
                }

                override fun onFailure(call: Call<Tokens>, t: Throwable) {
                    Log.d("REQUESTS LOGIN", "User login error!");
                    Log.d("RE", t.toString());
                    Toast.makeText(
                        context,
                        "Wrong credentials!",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            })
        }

        fun search(searchText: String, customCallback: CustomCallback, context: Context){

            var access_token : String = "";
            var refresh_token : String = "";
            //sacuvaj token
            GlobalScope.launch(Dispatchers.IO) {
                access_token =  DataStoreManager.getStringValue(context, "access_token");
                refresh_token = DataStoreManager.getStringValue(context, "refresh_token");
                Log.d("GOT CREDS", "${access_token}, ${refresh_token}")

                val call: Call<List<LocationDTO>> =
                    requestsInterface.searchLocation("Bearer " + access_token, searchText);


                call.enqueue(object : Callback<List<LocationDTO>> {
                    override fun onResponse(
                        call: Call<List<LocationDTO>>,
                        response: Response<List<LocationDTO>>
                    ) {
//                    val lista = mutableListOf<LocationDTO>()
                        var lista : List<LocationDTO>  = emptyList();
//                    var lista : MutableList<LocationDTO>  = MutableList<LocationDTO>(0);

                        response.body()?.forEach { loc ->
                            lista += loc;
                        }
                        Log.d("LOCATION SEARCH", "Lista je ${lista}");
                        customCallback.onSucess(lista);
                    }

                    override fun onFailure(call: Call<List<LocationDTO>>, t: Throwable) {
                        println(t.message)
                    }
                })
            }

        }

        fun getAll(customCallback: CustomCallback, context: Context){

            var access_token : String = "";
            var refresh_token : String = "";
            //sacuvaj token
            GlobalScope.launch(Dispatchers.IO) {
                access_token =  DataStoreManager.getStringValue(context, "access_token");
                refresh_token = DataStoreManager.getStringValue(context, "refresh_token");
                Log.d("GOT CREDS", "${access_token}, ${refresh_token}")

                val call: Call<List<LocationDTO>> =
                    requestsInterface.getAll("Bearer " + access_token);


                call.enqueue(object : Callback<List<LocationDTO>> {
                    override fun onResponse(
                        call: Call<List<LocationDTO>>,
                        response: Response<List<LocationDTO>>
                    ) {
//                    val lista = mutableListOf<LocationDTO>()
                        var lista : List<LocationDTO>  = emptyList();
//                    var lista : MutableList<LocationDTO>  = MutableList<LocationDTO>(0);

                        response.body()?.forEach { loc ->
                            lista += loc;
                        }
                        customCallback.onSucess(lista);
                    }

                    override fun onFailure(call: Call<List<LocationDTO>>, t: Throwable) {
                        println(t.message)
                    }
                })
            }

        }
    }
}