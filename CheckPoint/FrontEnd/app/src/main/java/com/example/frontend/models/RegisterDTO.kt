package com.example.frontend.models

data class RegisterDTO(
    val email: String,
    val name: String,
    val password: String,
    val passwordConfirm: String
)