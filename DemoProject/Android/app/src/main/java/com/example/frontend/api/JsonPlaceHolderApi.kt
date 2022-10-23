package com.example.frontend.api

import com.example.frontend.models.Movie
import retrofit2.Call;
import retrofit2.http.GET

interface JsonPlaceHolderApi {
    @GET("movies")
    fun getMovies(): Call<List<Movie>>;
}