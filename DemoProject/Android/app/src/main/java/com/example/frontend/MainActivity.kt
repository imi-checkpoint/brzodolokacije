package com.example.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.frontend.adapters.MovieAdapter
import com.example.frontend.api.JsonPlaceHolderApi
import com.example.frontend.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var retrofit : Retrofit;

        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.16:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        var jsonPlaceHolderApi : JsonPlaceHolderApi;

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java);

        var call : Call<List<Movie>>;

        call = jsonPlaceHolderApi.getMovies();

        val nes = this
        call.enqueue(object:Callback<List<Movie>>{
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                var movies : List<Movie>
                movies = response.body()!!;
                Log.i("test",movies.toString())
                /*for(movie in movies){
                    var content : String = "";
                    content += "ID: " + movie.id + "\n";
                    content += "Name: " + movie.name + "\n";

                    textViewResult.append(content);
                }*/
                listView = findViewById(R.id.list_id)
                val adapter = MovieAdapter(nes,movies)
                listView.adapter = adapter
                listView.showContextMenu()
                Log.i("test",listView.count.toString())
                listView.setOnItemClickListener{_,_,position,_->
                    val selectedMovie = movies[position]
                    println(selectedMovie.id)
                    println(selectedMovie.name)
                    println(selectedMovie.genre)
                    println(position)
                    val detailIntent = MovieDetailActivity.newIntent(nes,selectedMovie)
                    startActivity(detailIntent)
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {

            }

        })

        val btn_add = findViewById<Button>(R.id.btn_add)
        btn_add.setOnClickListener {
            val intent = Intent(this,AddMovieActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}