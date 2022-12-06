package com.example.frontend.presentation.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.frontend.domain.model.Post
import com.example.frontend.presentation.destinations.*
import com.example.frontend.presentation.location.LocationCard
import com.example.frontend.presentation.map.MapWindow
import com.example.frontend.presentation.profile.components.UserPostsState
import com.example.frontend.presentation.profile.components.ProfileDataState
import com.example.frontend.presentation.profile.components.ProfilePictureState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun ProfileScreen(
    userId : Long,
    username : String,
    navigator : DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
)
{
    val context = LocalContext.current
    val state = viewModel.state.value
    val pictureState = viewModel.pictureState.value
    val postsState = viewModel.postsState.value

    viewModel.proveriConstants()

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    if(state.error.contains("403") || pictureState.error.contains("403") || postsState.error.contains("403")){
        navigator.navigate(LoginScreenDestination){
            popUpTo(MainLocationScreenDestination.route){
                inclusive = true;
            }
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            viewModel.getProfileData();
        }
    ){

        if(state.isLoading || pictureState.isLoading || postsState.isLoading){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator();
            }
        }
        else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopBar(
                    name =
                    if(viewModel.savedUserId == viewModel.loginUserId)
                        viewModel.username
                    else
                        username
                    ,
                    modifier = Modifier.padding(20.dp),
                    navigator = navigator,
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(4.dp))

                ProfileSection(navigator, state, pictureState, viewModel.savedUserId);
                Spacer(modifier = Modifier.height(25.dp))

                //ako nije moj profil
                if(viewModel.savedUserId != viewModel.loginUserId){
                    ButtonSection(viewModel, modifier = Modifier.fillMaxWidth());
                    Spacer(modifier = Modifier.height(25.dp))
                }

                if(viewModel.savedUserId != 0L){
                    UserPostsSection(postsState, viewModel.savedUserId, navigator);
                }
            }
        }

    }
}

@Composable
fun TopBar(
    name : String,
    modifier: Modifier = Modifier,
    navigator : DestinationsNavigator,
    viewModel: ProfileViewModel
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.Start
        ){
            IconButton(onClick = {
                navigator.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = modifier.size(24.dp),
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = name,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Row(
            horizontalArrangement = Arrangement.End
        ){
            if(viewModel.savedUserId == viewModel.loginUserId)
            {
                IconButton(onClick = {
                    navigator.navigate(
                        ProfileSettingsScreenDestination()
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = modifier.size(24.dp)
                    )
                }
            }
            else{
                IconButton(onClick = {
                    /* */
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Back",
                        tint = Color.Transparent,
                        modifier = modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileSection(
    navigator : DestinationsNavigator,
    state : ProfileDataState,
    pictureState : ProfilePictureState,
    userId : Long,
    modifier: Modifier = Modifier,
)
{
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            val painter = rememberImagePainter(
                data = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                builder = {}
            )
            if(pictureState.error != ""){
                Image(
                    painter = painter,
                    contentDescription = "Profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(CircleShape),
                )
            }
            else{
                val photo = pictureState.profilePicture;
                val decoder = Base64.getDecoder()
                val photoBytes = decoder.decode(photo)
                if(photoBytes.size>1){
                    val mapa: Bitmap = BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.size)
                    print(mapa.byteCount)
                    if(mapa!=null){
                        Image(
                            bitmap = mapa.asImageBitmap(),
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .clip(CircleShape),
                            contentDescription ="Profile image" ,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                else{
                    //def picture
                    Image(
                        painter = painter,
                        contentDescription = "Profile image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .clip(CircleShape),
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))

            StatSection(navigator, state, userId, modifier.weight(7f))

        }
    }
}

@Composable
fun RoundImage(
    image: Painter,
    modifier: Modifier = Modifier
)
{
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)
    )
}

@Composable
fun StatSection(
    navigator : DestinationsNavigator,
    state : ProfileDataState,
    userId: Long,
    modifier: Modifier = Modifier
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ){
        if(state.error.isNotBlank()){
            Text("Error!");
        }

        if(state.profileData != null){
            ProfileStat(numberText = state.profileData.postCount.toString(), text = "Posts",
            onClick = {})
            ProfileStat(numberText = state.profileData.followersCount.toString(), text = "Followers",
            onClick = {
                navigator.navigate(
                    UserListScreenDestination("followers", userId)
                )
            })
            ProfileStat(numberText = state.profileData.followingCount.toString(), text = "Following",
            onClick = {
                navigator.navigate(
                    UserListScreenDestination("following", userId)
                )
            })
        }
    }
}

@Composable
fun ProfileStat(
    numberText : String,
    text : String,
    modifier: Modifier = Modifier,
    onClick : () -> Unit = {}
)
{
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable{
                onClick()
            }
    )    {
        Text(
            text = numberText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = text)
    }
}

@Composable
fun ButtonSection(
    viewModel : ProfileViewModel,
    modifier: Modifier = Modifier
){
    val minWidth = 120.dp
    val height = 35.dp

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
//            .background(color = Color.Magenta)

    ){
        ActionButton(
            text = "Follow",
            icon = Icons.Default.Add,
            viewModel = viewModel,
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )

        ActionButton(
            text = "Message",
            viewModel = viewModel,
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    viewModel : ProfileViewModel,
    text : String? = null,
    icon : ImageVector? = null
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(6.dp)
            .clickable {
                if (text == "Follow") {
                    //Constants.refreshPhotoConstant = viewModel.loginUserId
                    viewModel.followUnfollowUser();
                } else if (text == "Message") {
                    //message this user

                }
            }
    ){
        if(text != null){
            Text(
                text =
                if(text == "Follow")
                {
                    if(viewModel.state.value.profileData?.amFollowing == true)
                        "Unfollow"
                    else
                        "Follow"
                }
                else text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }

        if(icon != null){
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Composable
fun UserPostsSection(
    postsState : UserPostsState,
    userId: Long,
    navigator : DestinationsNavigator
)
{
    var list by remember{ mutableStateOf(true) }
    var map by remember{ mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Button(onClick = {
            list = true;
            map = false;
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = if(list == true) Color.DarkGray else Color.LightGray,
                contentColor = Color.White
            )
        ) {
            Text(text = "LIST" , Modifier.padding(8.dp))
        }

        Button(onClick = {
            list = false;
            map = true;
        },
            colors = ButtonDefaults.buttonColors(
                containerColor =if(map == true) Color.DarkGray else Color.LightGray,
                contentColor = Color.White
            )
        ) {
            Text(text = "MAP" , Modifier.padding(8.dp))
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        if(postsState.userPosts!=null){
            if(list){
                PostsSection(userId = userId, postsState.userPosts, navigator);
            }
            if(map){
                MapSection(userId = userId, postsState.userPosts,navigator);
            }
        }
        else{
            Text("ERROR");
        }
    }
}

@Composable
fun PostsSection(
    userId : Long,
    posts: List<Post>,
    navigator : DestinationsNavigator
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        if(posts.size < 1)
        {
            Text("No posts yet");
        }
        else{


            var searchPosts by remember{ mutableStateOf(posts) }
            var searchText by remember{ mutableStateOf("") }

            val trailingIconView = @Composable {
                IconButton(onClick = {
                    searchText = ""
                    searchPosts = posts;
                }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription ="",
                        tint = Color.Black
                    )
                }
            }

            TextField(
                value = searchText,
                trailingIcon = if(searchText.isNotBlank()) trailingIconView else null,
                onValueChange = {
                    searchText = it
                    if(searchText!="")
                        searchPosts = posts.filter { p ->
                            p.location.name.lowercase().contains(searchText.trim().lowercase());
                        }
                    else
                        searchPosts = posts;
                },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null)},
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text("Search user posts")
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ){
                items(searchPosts){
                        post -> PostCard(post = post, navigator = navigator)
                }
            }


        }
    }
}

@Composable
fun PostCard(
    post : Post,
    navigator : DestinationsNavigator
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigator.navigate(
                    PostScreenDestination(post.postId)
                )
            }
    ){
        Text(post.location.name);
    }
}

@Composable
fun MapSection(
    userId : Long,
    posts: List<Post>,
    navigator : DestinationsNavigator
)
{
    Column(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            )
            .border(
                width = Dp.Hairline,
                color = Color.Transparent,
                shape = RoundedCornerShape(5.dp)
            )
    ) {
        MapWindow(userId, posts)
    }
}