package com.example.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.Screen
import com.example.frontend.api.CustomCallback
import com.example.frontend.api.Requests
import com.example.frontend.models.LocationDTO
import com.example.frontend.models.PostDTO


@Composable
fun MainPostsScreen(navController: NavController, locationId:Long){
    PostsScreen(navController = navController, location =locationId )
}

@Composable
fun PostsScreen(navController: NavController,location:Long){
    val context = LocalContext.current
    var lista:List<PostDTO> by remember { mutableStateOf (emptyList())}
    Requests.getPostsFromLocation(object : CustomCallback<List<PostDTO>>{
        override fun onSucess(locList: List<PostDTO>) {
            lista = locList;
            println("Prosao zahtev")
        }
        override fun onFailure() {println("Pao zahtev")} }, context,location)
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(20.dp),
    ){
            Text("test")
            IconButton(onClick = { navController.navigate(Screen.MainSearchScreen.route) }) {
                Icon(Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = Color.Black)
            }

        LazyColumn{
            items(lista){
                each-> postCard(post = each, navController = navController)
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun postCard(post:PostDTO,navController: NavController)
{
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick = {
            navController.navigate(Screen.ProfileScreen.route);
        }
    ){
        Row {
            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = post.user.username)
                Text(text = post.description)
            }
        }
    }
}