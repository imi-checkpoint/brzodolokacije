package com.example.frontend.presentation.posts

import Constants
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.domain.model.Photo
import com.example.frontend.domain.model.Post
import com.example.frontend.presentation.destinations.LoginScreenDestination
import com.example.frontend.presentation.destinations.MainLocationScreenDestination
import com.example.frontend.presentation.destinations.PostScreenDestination
import com.example.frontend.presentation.destinations.ProfileScreenDestination
import com.example.frontend.presentation.posts.components.PostStringState
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
    var sort = remember {
        mutableStateOf(Constants.sort)
    }
    viewModel.proveriConstants();
    var expanded = remember { mutableStateOf(false) }
    val state = viewModel.state.value
    val stateDelete = viewModel.stateDelete.value

    if(state.error.contains("403") || stateDelete.error.contains("403")){
        navigator.navigate(LoginScreenDestination){
            popUpTo(MainLocationScreenDestination.route){
                inclusive = true;
            }
        }
    }
    
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
        Row(Modifier.fillMaxWidth()){
            Text(viewModel.nazivSorta()
                )
            Spacer(Modifier.weight(1f))
            Button(onClick = { expanded.value = true },Modifier.wrapContentWidth(),
                ) {
                Text("Sort")
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
                    .wrapContentWidth()
                    .height(300.dp),
                ) {
                listOf("Date dsc","Date asc","Likes asc","Likes dsc","Comments asc","Comments dsc").forEachIndexed  { index, item ->
                    DropdownMenuItem(onClick = {
                        Constants.sort = index
                        expanded.value = false
                    }
                    ) {
                        Text(item)
                    }
                }
            }
        }
        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else if(state.error!=""){
            Text("An error occured while loading posts!");
        }
        else{
            AllPosts(viewModel.getPosts(), navigator, viewModel, stateDelete)
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
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
        onClick ={
            navigator.navigate(PostScreenDestination(post.postId))
        }
    ){
        Row {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
                                navigator.navigate(ProfileScreenDestination(post.appUserId, post.appUserUsername))
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        val photo = post.image
                        val decoder = Base64.getDecoder()
                        val photoBytes = decoder.decode(photo)
                        if(photoBytes.size>1){
                            val mapa: Bitmap = BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.size)
                            print(mapa.byteCount)
                            if(mapa!=null){
                                Image(
                                    bitmap = mapa.asImageBitmap(),
                                    contentDescription = "Profile image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(35.dp)
                                        .width(35.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = post.appUserUsername,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = post.date,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    if(viewModel.loginUserId == post.appUserId) {
                       DeletePostButton(post = post, viewModel = viewModel, stateDelete = stateDelete)
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
                    text = post.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Row {
                        Text(
                            text = "${post.numberOfComments} comments",
                            style = MaterialTheme.typography.labelMedium
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
            .height(150.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
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
    post: Post,
    viewModel: PostsViewModel,
    stateDelete: PostStringState
) {

    IconButton(
        onClick = {
            viewModel.deletePostById(post.postId, post.location.id)
        },
        modifier = Modifier
            .clip(RectangleShape)
            .border(BorderStroke(1.dp, Color.LightGray))
            .size(30.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete post",
            tint = MaterialTheme.colorScheme.outline
        )
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
        post.isLiked = !post.isLiked
        if (post.isLiked)
            post.numberOfLikes += 1;
        else
            post.numberOfLikes -= 1;
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








