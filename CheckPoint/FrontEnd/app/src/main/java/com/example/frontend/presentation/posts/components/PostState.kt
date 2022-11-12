package com.example.frontend.presentation.posts.components

import com.example.frontend.domain.model.Post

data class PostState (
    val isLoading: Boolean = false,
    val posts: List<Post>? = emptyList(), //ako je lista, onda emptyList()
    val error: String = ""
)