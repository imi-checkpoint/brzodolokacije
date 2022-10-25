package com.example.frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.api.JsonPlaceHolderApi
import com.example.frontend.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MovieDetailActivity: AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val id = intent.extras?.getLong(EXTRA_ID)
        val name = intent.extras?.getString(EXTRA_NAME)
        val genre = intent.extras?.getString(EXTRA_GENRE)

        val idField = findViewById<TextView>(R.id.textView)
        idField.setText(id.toString())
        val nameField = findViewById<EditText>(R.id.textView2)
        nameField.setText(name)
        val genreField = findViewById<EditText>(R.id.textView3)
        genreField.setText(genre)

        val btn_delete = findViewById<Button>(R.id.btn_delete)
        btn_delete.setOnClickListener {
            deleteMethod(id!!)
        }
        val btn_update = findViewById<Button>(R.id.btn_update)
        btn_update.setOnClickListener {
            var movie :Movie = Movie(id!!,nameField.text.toString(),genreField.text.toString())
            update(this,movie)
        }
        val btn_back = findViewById<Button>(R.id.btn_back)
        btn_back.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_ID = "id"
        const val EXTRA_NAME = "name"
        const val EXTRA_GENRE = "genre"

        fun newIntent(context: Context, movie: Movie): Intent {
            val detailIntent = Intent(context, MovieDetailActivity::class.java)
            detailIntent.putExtra(EXTRA_ID, movie.id)
            detailIntent.putExtra(EXTRA_NAME, movie.name)
            detailIntent.putExtra(EXTRA_GENRE, movie.genre)

            return detailIntent
        }
    }

    fun deleteMethod(movieId: Long) {

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        var jsonPlaceHolderApi : JsonPlaceHolderApi;
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java);

        var call : Call<String>;
        call = jsonPlaceHolderApi.deleteMovie(movieId)

        val nes = this
        call.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var res : String = response.body()!!;
                Log.i("!!!!!!!!!!! ~~~~~~  test delete: ", res)
                //vrati na pocetnu
                val intent = Intent(nes,MainActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
    }



    fun update(context:Context,movie:Movie){
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var jsonPlaceHolderApi : JsonPlaceHolderApi;
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java);

        var call : Call<String>;
        call = jsonPlaceHolderApi.updateMovie(movie)
        val nes = this
        call.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var res : String = response.body()!!;
                Log.i("!!!!!!!!!!! ~~~~~~  test update: ", res)
                //vrati na pocetnu
                val intent = Intent(context,MainActivity::class.java)
                startActivity(intent)
                finish()
                Log.i("test123","test123")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("test123",t.toString())
                Log.i("test123",call.request().toString())
            }
        })
    }
    }
