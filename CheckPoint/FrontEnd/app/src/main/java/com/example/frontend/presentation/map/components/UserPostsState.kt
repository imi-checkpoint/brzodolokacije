package com.example.frontend.presentation.map.components

import com.example.frontend.domain.model.Post

data class UserPostsState (
    val isLoading: Boolean = false,
    val userPosts: List<Post>? = emptyList(),
    val error: String = ""
)