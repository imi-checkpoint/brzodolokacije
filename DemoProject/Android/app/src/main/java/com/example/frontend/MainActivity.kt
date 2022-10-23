package com.example.frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.frontend.api.JsonPlaceHolderApi
import com.example.frontend.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var textViewResult : TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewResult = findViewById(R.id.text_view_result);
        var retrofit : Retrofit;

        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.7:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        var jsonPlaceHolderApi : JsonPlaceHolderApi;

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java);

        var call : Call<List<Movie>>;

        call = jsonPlaceHolderApi.getMovies();

        call.enqueue(object:Callback<List<Movie>>{
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if(!response.isSuccessful){
                    textViewResult.setText("Code: " +  response.code());
                    return;
                }
                var movies : List<Movie>
                movies = response.body()!!;

                for(movie in movies){
                    var content : String = "";
                    content += "ID: " + movie.id + "\n";
                    content += "Name: " + movie.name + "\n";
                    content += "Genre: " + movie.genre + "\n";

                    textViewResult.append(content);
                }

            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                textViewResult.setText(t.message);
            }

        })
    }
}