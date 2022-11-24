package com.example.frontend.presentation.newpost.components

import android.graphics.Bitmap
import com.example.frontend.common.Resource

data class NovPostState(
    val error: String = "",
    val isLoading: Boolean = false,
    val slike: List<SlikaState> = emptyList()
)
