package com.example.frontend.api

import com.example.frontend.models.Movie
import okhttp3.internal.http.HttpHeaders
import retrofit2.Call;
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface JsonPlaceHolderApi {
    @GET("movies")
    fun getMovies(): Call<List<Movie>>;

    @DELETE("delete/{id}")
    fun deleteMovie(@Path("id") id: Long): Call<String>

    @POST("save_movie")
    fun addMovie(@Body movie: Movie): Call<String>

    @Headers("Content-Type: application/json")
    @PUT("update_movie")
    fun updateMovie(@Body movie: Movie): Call<String>
}