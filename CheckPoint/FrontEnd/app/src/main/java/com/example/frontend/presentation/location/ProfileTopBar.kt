package com.example.frontend.presentation.location

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProfileTopBar()
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Row(){
            IconButton(onClick = {
//                navController.navigate(Screen.ProfileScreen.route);
            }) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "",
                    tint = Color.Black
                )
            }

            IconButton(onClick = {
//                go to messages
            }) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
    }
}