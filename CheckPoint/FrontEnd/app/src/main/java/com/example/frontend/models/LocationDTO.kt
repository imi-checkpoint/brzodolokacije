package com.example.frontend.models

import com.squareup.moshi.Json

data class LocationDTO(
    @field:Json(name = "id") val id:Long,
    @field:Json(name = "name") val name:String,
    @field:Json(name = "coordinateX") val coordinateX:Double,
    @field:Json(name = "coordinateY") val coordinateY: Double
)
