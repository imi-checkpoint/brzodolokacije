package com.example.frontend.presentation.profile.components

import com.example.frontend.domain.model.Post

data class ProfileState(
    val isLoading: Boolean = false,
//    val posts: List<Post>? = emptyList(), //ako je lista, onda emptyList()
    val error: String = ""
)
