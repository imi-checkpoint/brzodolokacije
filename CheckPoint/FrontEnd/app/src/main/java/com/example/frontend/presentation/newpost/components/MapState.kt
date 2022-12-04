package com.example.frontend.presentation.newpost.components

import com.example.frontend.data.remote.dto.LocationDTO

data class MapState(
    val location: LocationDTO? = null,
    val isLoading: Boolean = false
)
