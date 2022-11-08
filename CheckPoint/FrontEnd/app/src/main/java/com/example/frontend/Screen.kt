package com.example.frontend

import com.example.frontend.models.LocationDTO

sealed class Screen(val route : String) {
    object LoginScreen : Screen("login_screen");
    object RegisterScreen : Screen("register_screen");
    object MainSearchScreen : Screen("mainSearch_screen");
    object ProfileScreen : Screen("profile_screen");
    object PostsScreen : Screen("posts_screen");
    fun withArgs(vararg args : String) : String {
        return buildString{
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}