package com.example.frontend.presentation.post

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun PostScreen(
    navController: NavController,
    viewModel: PostViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value

    if(state.post!=null){
        Text("Hello post - ${state.post.description}")
    }
}