package com.example.frontend.presentation.MainFeed

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.domain.model.Photo
import com.example.frontend.domain.model.Post
import com.example.frontend.presentation.destinations.*
import com.example.frontend.presentation.location.LocationViewModel
import com.example.frontend.presentation.posts.AllPosts
import com.example.frontend.presentation.posts.PostCard
import com.example.frontend.presentation.posts.PostsViewModel
import com.example.frontend.presentation.posts.components.PostStringState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun MainFeedScreen(
    navigator: DestinationsNavigator,
    viewModel : MainFeedViewModel = hiltViewModel()
) {

    var sort = remember {
        mutableStateOf(Constants.sort)
    }

    viewModel.proveriConstants()
    var expanded = remember { mutableStateOf(false) }

    val state = viewModel.state.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        ProfileMTopBar(navigator,viewModel)
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (state.error != "") {
            Text("An error occured while loading posts!");
        } else {
            if(viewModel.getPosts().isNotEmpty()) {
                Row(Modifier.fillMaxWidth()) {
//                    Text(
//                        text = viewModel.nazivSorta(),
//                        modifier = Modifier.clickable{
//                            expanded.value = true
//                        }
//                    )

                    OutlinedTextField(
                        value = viewModel.nazivSorta(),
                        onValueChange = {},
                        Modifier.wrapContentWidth(),
                        readOnly = true,
                        label = {
                            Text(text = "Sort")
                        },
                        trailingIcon = {
                            Icon(
                                if(expanded.value == false) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = null,
                                Modifier.clickable  {
                                    Log.d("DROP", "CLICK");
                                    expanded.value = true
                                }
                            )
                        }
                    );

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(300.dp),
                    ) {
                        listOf(
                            "Date dsc",
                            "Date asc",
                            "Likes asc",
                            "Likes dsc",
                            "Comments asc",
                            "Comments dsc"
                        ).forEachIndexed { index, item ->
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
            }
            AllMPosts(viewModel.getPosts(), navigator, viewModel)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllMPosts(
    posts : List<Post>?,
    navigator: DestinationsNavigator,
    viewModel : MainFeedViewModel,
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
                    post -> PostMCard(post, navigator, viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostMCard(
    post : Post,
    navigator : DestinationsNavigator,
    viewModel : MainFeedViewModel,
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
                                        .height(25.dp)
                                        .width(25.dp)
                                        .clip(CircleShape),
                                )
                            }
                        }
                        Text(
                            text = "${post.appUserUsername}",
                            color = Color.DarkGray
                        )
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
            .height(150.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        println(photo.photo.data.toByteArray().size)
        val decoder = Base64.getDecoder()
        val photoBytes = decoder.decode(photo.photo.data)
        if(photoBytes.size>1){
            val mapa: Bitmap = BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.size)
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
fun LikeOrUnlikePostButton(
    post : Post,
    viewModel: MainFeedViewModel,
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

@Composable
fun ProfileMTopBar(
    navigator : DestinationsNavigator,
    viewModel: MainFeedViewModel
)
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
                navigator.navigate(
                    MainLocationScreenDestination()
                )
            }){
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
            IconButton(onClick = {
                navigator.navigate(
                    NovPostScreenDestination()
                )
            }) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "",
                    tint = Color.Black
                )
            }

            IconButton(onClick = {
                navigator.navigate(
                    ProfileScreenDestination(viewModel.loginUserId, "")
                );
            }) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "",
                    tint = Color.Black
                )
            }

            IconButton(onClick = {
//                viewModel.getAllLocations()
//                navigator.navigate() //navigate to messages
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
