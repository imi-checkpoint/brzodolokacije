package com.example.frontend.common.navigation

sealed class Screen(val route : String) {
    object LoginScreen : Screen("login_screen");
    object RegisterScreen : Screen("register_screen");
    object MainLocationScreen : Screen("mainLocation_screen");
    object ProfileScreen : Screen("profile_screen");
    object PostsScreen : Screen("posts_screen");
    object UserListScreen : Screen("userList_screen");
    object NovPostScreen : Screen("newPost_screen");
    object PostScreen : Screen("post_screen");

    fun withArgs(vararg args : Long) : String {
        return buildString{
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withArgs(vararg args : String) : String {
        return buildString{
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

}