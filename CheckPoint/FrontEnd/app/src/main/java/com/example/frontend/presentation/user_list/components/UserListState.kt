package com.example.frontend.presentation.user_list.components

import com.example.frontend.domain.model.User

data class UserListState(
    val isLoading : Boolean = false,
    val users : List<User>? = emptyList(),
    val error : String = ""
)