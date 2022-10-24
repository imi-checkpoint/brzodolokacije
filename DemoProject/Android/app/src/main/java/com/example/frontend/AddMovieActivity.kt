package com.example.frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.api.JsonPlaceHolderApi
import com.example.frontend.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class AddMovieActivity: AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_movie)

        val btn_save = findViewById<Button>(R.id.btn_save)
        btn_save.setOnClickListener {
            val name = findViewById<EditText>(R.id.nameInput).text
            val genre = findViewById<EditText>(R.id.genreInput).text

            if (name.isNotEmpty() && genre.isNotEmpty()) {
                var movie : Movie = Movie(0,name.toString().trim(),genre.toString().trim())
                saveMovie(this,movie)
            }
        }
    }
    fun saveMovie(context: Context, movie:Movie){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.16:8080/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var jsonPlaceHolderApi : JsonPlaceHolderApi;
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java);

        var call : Call<String>;
        call = jsonPlaceHolderApi.addMovie(movie)
        val nes = this
        call.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var res : String = response.body()!!;
                Log.i("!!!!!!!!!!! ~~~~~~  test save: ", res)
                //vrati na pocetnu
                val intent = Intent(context,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
    }
}