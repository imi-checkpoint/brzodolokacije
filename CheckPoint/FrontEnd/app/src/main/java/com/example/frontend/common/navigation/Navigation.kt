package com.example.frontend.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.frontend.presentation.TestScreen
import com.example.frontend.presentation.login.LoginScreen
import com.example.frontend.presentation.register.RegisterScreen
import com.example.frontend.presentation.location.MainLocationScreen
import com.example.frontend.presentation.posts.PostsScreen
import com.example.frontend.presentation.profile.ProfileScreen
import com.example.frontend.presentation.user_list.UserListScreen

@Composable
fun Navigation()
{
    val navController = rememberNavController();
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route){
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.MainLocationScreen.route){
            MainLocationScreen(navController = navController)
        }
        composable(
            route = Screen.ProfileScreen.route + "/{userId}",
            arguments = listOf(
                navArgument("userId"){
                    type = NavType.LongType
                    defaultValue = 0
                    nullable = false
                }
            )
        ){
            entry ->
                ProfileScreen(navController = navController)
        }
        composable(
            route = Screen.PostsScreen.route + "/{locationId}",
            arguments = listOf(
                navArgument("locationId"){
                    type = NavType.LongType
                    defaultValue = 0
                    nullable = false
                }
            )
        ){
                entry ->
            PostsScreen(navController = navController)
        }
        composable(
            route = Screen.UserListScreen.route + "/{userTypeList}/{userId}",
            arguments = listOf(
                navArgument("userTypeList"){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                },
                navArgument("userId"){
                    type = NavType.LongType
                    defaultValue = 0
                    nullable = false
                }
            )
        ){
            entry ->
                UserListScreen(navController = navController)
        }
    }
}

fun NavHostController.navigateAndClean(route : String){
    navigate(route = route){
        popUpTo(graph.startDestinationId){
            inclusive = true;
        }
        graph.setStartDestination(route);
    }
}