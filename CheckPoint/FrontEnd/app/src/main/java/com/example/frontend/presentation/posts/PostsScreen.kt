package com.example.frontend.presentation.posts

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.domain.model.Photo
import com.example.frontend.domain.model.Post
import com.example.frontend.presentation.destinations.PostScreenDestination
import com.example.frontend.presentation.destinations.ProfileScreenDestination
import com.example.frontend.presentation.posts.components.PostStringState
import com.example.frontend.presentation.location.ProfileTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun PostsScreen(
    locationId : Long,
    navigator: DestinationsNavigator,
    viewModel : PostsViewModel = hiltViewModel()
)
{

    val state = viewModel.state.value
    val stateDelete = viewModel.stateDelete.value
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        IconButton(onClick = {
            navigator.popBackStack()
        }) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.DarkGray)
        }
        
        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else if(state.error!=""){
            Text("An error occured while loading posts!");
        }
        else{
            AllPosts(state.posts, navigator, viewModel, stateDelete)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllPosts(
    posts : List<Post>?,
    navigator: DestinationsNavigator,
    viewModel : PostsViewModel,
    stateDelete: PostStringState
)
{
    if(posts == null || posts.size == 0){
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
                post -> PostCard(post, navigator, viewModel, stateDelete)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostCard(
    post : Post,
    navigator : DestinationsNavigator,
    viewModel : PostsViewModel,
    stateDelete: PostStringState
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
            navigator.navigate(
                PostScreenDestination(post.postId)
            )
        }
    ){
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable{
                                navigator.navigate(ProfileScreenDestination(post.appUserId))
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "",
                            tint = Color.DarkGray,
                        )
                        Text(
                            text = "${post.appUserUsername}",
                            color = Color.DarkGray
                        )
                    }
                   Row(
                       verticalAlignment = Alignment.CenterVertically
                   ){
                       if(viewModel.loginUserId == post.appUserId)
                           DeletePostButton(postId = post.postId, viewModel = viewModel, stateDelete = stateDelete)
                   }
                }

                Row(){
                    if(post.photos.size > 0){
                        Spacer(modifier = Modifier.height(5.dp))
                        PhotoCard(photo = post.photos[0], navigator)
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "${post.description}",
                    color = Color.DarkGray
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        LikeOrUnlikePostButton(post = post, viewModel = viewModel, stateLikeOrUnlike = viewModel.stateLikeOrUnlike.value)
                        Text(
                            text = "${post.numberOfLikes} likes",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Row {
                        Text(
                            text = "${post.numberOfComments} comments",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
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
    navigator: DestinationsNavigator,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        println(photo.photo.data.toByteArray().size)
        val decoder = Base64.getDecoder()
        val photoBytes = decoder.decode(photo.photo.data)
        if(photoBytes.size>1){
            val mapa:Bitmap = BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.size)
            print(mapa.byteCount)
            if(mapa!=null){
                Image(
                    bitmap = mapa.asImageBitmap(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeletePostButton(
    postId: Long,
    viewModel: PostsViewModel,
    stateDelete: PostStringState
) {
    IconButton(onClick = {
        viewModel.deletePostById(postId)
    }) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "Delete post",
            tint = Color(0xfff44336)
        )
    }
    if(stateDelete.isLoading){
    }
    else if(stateDelete.error!=""){
        Text("An error occured while deleting post!");
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LikeOrUnlikePostButton(
    post : Post,
    viewModel: PostsViewModel,
    stateLikeOrUnlike: PostStringState
) {
    IconButton(onClick = {
        viewModel.likeOrUnlikePostById(post.postId)
    }) {
        Icon(
            if(post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if(post.isLiked) "Unlike" else "Like",
            tint = if(post.isLiked) Color.Red else Color.DarkGray
        )
    }
    if(stateLikeOrUnlike.isLoading){
    }
    else if(stateLikeOrUnlike.error!=""){
        Text("An error occured while like/unlike post!");
    }
}








