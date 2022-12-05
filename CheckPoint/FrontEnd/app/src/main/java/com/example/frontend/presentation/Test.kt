package com.example.frontend.presentation

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun TestScreen(
    navController: NavController
)
{
    Text("Test");
    Log.d("Test screen", "Test screen invoked")
}
