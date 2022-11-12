package com.example.frontend.common.navigation

sealed class Screen(val route : String) {
    object LoginScreen : Screen("login_screen");
    object RegisterScreen : Screen("register_screen");
    object MainLocationScreen : Screen("mainLocation_screen");
    object ProfileScreen : Screen("profile_screen");
    object PostsScreen : Screen("posts_screen");

    fun withArgs(vararg args : Long) : String {
        return buildString{
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

}