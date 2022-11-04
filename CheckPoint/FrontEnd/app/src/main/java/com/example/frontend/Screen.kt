package com.example.frontend

sealed class Screen(val route : String) {
    object MainScreen : Screen("main_screen");
    object LoginScreen : Screen("login_screen");
    object RegisterScreen : Screen("register_screen");
    object SearchLocationScreen : Screen("searchLocation_screen");

    fun withArgs(vararg args : String) : String {
        return buildString{
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}