package com.example.frontend.presentation.posts

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.common.navigation.Screen
import com.example.frontend.domain.model.Location
import com.example.frontend.domain.model.Photo
import com.example.frontend.domain.model.Post
import com.example.frontend.presentation.posts.components.PostCardState
import java.util.Base64

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
            AllPosts(state.posts, navController,viewModel)
        }
    }
}

@Composable
fun AllPosts(
    posts : List<Post>?,
    navController: NavController,
    viewModel : PostViewModel
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
                post -> PostCard(post, navController,viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostCard(
    post : Post,
    navController: NavController,
    viewModel : PostViewModel
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
                Text(post.appUserUsername)
                Text(post.description)
                LazyRow(){
                    items(post.photos){
                        photo->
                        PhotoCard(photo = photo, navController = navController)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PhotoCard(
    photo : Photo,
    navController: NavController
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        println(photo.photo.data.toByteArray().size)
        val decoder = Base64.getDecoder()
        val photoBytes = decoder.decode(photo.photo.data)
        if(photoBytes.size>1){
            val mapa:Bitmap = BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.size)
            print(mapa.byteCount)
            if(mapa!=null){
                Image(bitmap = mapa.asImageBitmap(), contentDescription ="" )
            }
        }
    }
}












