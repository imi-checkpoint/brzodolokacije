package com.example.frontend.presentation.posts.components

data class PostDeleteState (
    val isLoading: Boolean = false,
    val message: String = "",
    val error: String = ""
)
