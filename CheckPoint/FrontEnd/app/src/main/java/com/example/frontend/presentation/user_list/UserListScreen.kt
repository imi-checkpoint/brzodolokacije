package com.example.frontend.presentation.user_list

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: UserListViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value

    if(state.users != null){
        Log.d("USERS", state.users.toString());
    }

    Text("Hello")
}