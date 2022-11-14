package com.example.frontend.presentation.posts

import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.common.navigation.Screen
import com.example.frontend.domain.model.Post

@Composable
fun PostsScreen(
    navController: NavController,
    viewModel : PostViewModel = hiltViewModel()
)
{

    val state = viewModel.state.value
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.Black)
        }
        
        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else if(state.error!=""){
            Text("An error occured while loading posts!");
        }
        else{
            AllPosts(state.posts, navController)
        }
    }
}

@Composable
fun AllPosts(
    posts : List<Post>?,
    navController: NavController
)
{
    if(posts == null){
        Text(
            text = "No posts found!",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
    else{
        LazyColumn{
            items(posts){
                post -> PostCard(post, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostCard(
    post : Post,
    navController: NavController
)
{
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick ={
            navController.navigate(Screen.ProfileScreen.route)
        }
    ){
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(post.user.username)
                Text(post.description)
            }
        }
    }
}














