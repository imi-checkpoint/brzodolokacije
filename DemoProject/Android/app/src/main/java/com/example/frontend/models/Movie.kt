package com.example.frontend.models

import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("genre") val genre: String
)