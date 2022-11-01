package com.example.frontend.models

import com.google.gson.annotations.SerializedName

data class Tokens(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("refresh_token") val refresh_token: String
)