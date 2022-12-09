package com.example.frontend.presentation.post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.util.lerp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.frontend.domain.model.Comment
import com.example.frontend.domain.model.Photo
import com.example.frontend.domain.model.Post
import com.example.frontend.presentation.destinations.LoginScreenDestination
import com.example.frontend.presentation.destinations.MainLocationScreenDestination
import com.example.frontend.presentation.destinations.ProfileScreenDestination
import com.google.accompanist.pager.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import java.util.*
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun PostScreen(
    postId : Long,
    navigator : DestinationsNavigator,
    viewModel: PostViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value
    val stateGetComments = viewModel.stateGetComments.value
    val stateAddComment = viewModel.stateAddComment.value

    if(state.error.contains("403")){
        navigator.navigate(LoginScreenDestination){
            popUpTo(MainLocationScreenDestination.route){
                inclusive = true;
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp)
    ) {

        IconButton(onClick = {
            navigator.popBackStack()
        }) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.DarkGray
            )
        }

        if(state.isLoading || stateGetComments.isLoading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        else if (state.error != "" || stateGetComments.error != "") {
            Text("An error occured while loading this post!");
        }
        else if(state.post!=null){
            PostDetails(post = state.post, comments = stateGetComments.comments, navigator = navigator, viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostDetails(
    post: Post,
    comments: List<Comment>?,
    navigator: DestinationsNavigator,
    viewModel: PostViewModel
){

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {

        UsernameAndLike(post, navigator, viewModel)
        //photo slider
        if(post.photos.size > 0)
            ImagePagerSlider(post, post.photos)

        PostDescription(post)
        //DescriptionOrLocation(post, navigator)

        //PostMap(post, navigator)

        PostComments(post, comments, navigator, viewModel)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalPagerApi
@Composable
fun UsernameAndLike(
    post : Post,
    navigator: DestinationsNavigator,
    viewModel: PostViewModel
){
    val painterLoginPhoto = rememberImagePainter(
        data = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
        builder = {}
    )

    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp)
            .fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                modifier = Modifier
                    .clickable{
                        navigator.navigate(ProfileScreenDestination(post.appUserId, post.appUserUsername))
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterLoginPhoto,
                    contentDescription = "Profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(5.dp))
                /*
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
                 */
                Text(
                    text = post.appUserUsername,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                var likedBool by remember{ mutableStateOf(post.isLiked) }
                var likeCount by remember{ mutableStateOf(post.numberOfLikes) }

                IconButton(onClick = {
                    viewModel.likeOrUnlikePostById(post.postId)
                    likedBool = !likedBool
                    if (likedBool)
                        likeCount += 1;
                    else
                        likeCount -= 1;
                }) {
                    Icon(
                        if (likedBool) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (likedBool) "Unlike" else "Like",
                        tint = if (likedBool) Color.Red else Color.DarkGray,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = "${likeCount}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalPagerApi
@Composable
fun DescriptionOrLocation(
    post : Post,
    navigator: DestinationsNavigator
) {
    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp)
            .fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RectangleShape
    ) {

        //jedno ili drugo if else

        //description


        //location
        /*
        Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "",
                    tint = Color(0xff203f1e)
                )
                Text(
                    text = post.location.name,
                    style = MaterialTheme.typography.bodyMedium
                );
            }
         */

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalPagerApi
@Composable
fun ImagePagerSlider(
    post: Post,
    photos: List<Photo>
){

    val pagerState = rememberPagerState(
        pageCount = photos.size,
        initialPage = 0
    )

//    ako zelimo da se menjaju slike i same
//    LaunchedEffect(Unit){
//        while(true){
//            yield()
//            delay(2000)
//            pagerState.animateScrollToPage(
//                page = (pagerState.currentPage + 1) % pagerState.pageCount,
//                animationSpec = tween(600)
//            )
//        }
//    }

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 0.dp)
            .fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
            ){

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp, 30.dp, 0.dp, 15.dp)
                ) {
                        page->
                    Card(
                        modifier = Modifier
                            .graphicsLayer {
                                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                                lerp(
                                    start = 0.85f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }

                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                            .fillMaxWidth()
                            .padding(25.dp, 0.dp, 25.dp, 0.dp),
                        shape = RoundedCornerShape(15.dp)
                    ){

                        val photo = photos[page];

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray)
                                .align(Alignment.Center)
                        )

                        val decoder = Base64.getDecoder()
                        val photoBytes = decoder.decode(photo.photo.data)
                        if(photoBytes.size>1) {
                            val mapa: Bitmap =
                                BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
                            print(mapa.byteCount)
                            if (mapa != null) {
                                Image(
                                    bitmap = mapa.asImageBitmap(),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp, bottom = 15.dp),
                activeColor = Color.Blue,
                inactiveColor = Color.LightGray
            )
        }
    }
}

@Composable
fun PostDescription(
    post : Post
){
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp)
    ) {
        Text(
            text = post.description,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 15.dp)
        )
    }
}

@Composable
fun PostMap(
    post : Post,
    navigator: DestinationsNavigator
){
    Text("POST MAP")
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostComments(
    post : Post,
    comments: List<Comment>?,
    navigator: DestinationsNavigator,
    viewModel: PostViewModel
){

    AddFirstCommentCard(post, viewModel)

    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp)
            .fillMaxWidth()
            .heightIn(0.dp, 500.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (!(comments.isNullOrEmpty()))
            Column {
                comments.forEach{ comment ->
                    CommentCard(post, comment, navigator, viewModel)
                }
            }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddFirstCommentCard(
    post : Post,
    viewModel: PostViewModel
) {

    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 0.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var newCommentText by remember { mutableStateOf("") }
            val focusManager = LocalFocusManager.current

            BasicTextField(
                modifier = Modifier
                    .weight(3f)
                    .border(
                        BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(6.dp)
                    /*.background(
                        color = if (viewModel.replyToUsername.value == "") Color.White else Color.LightGray
                    )*/,
                value = newCommentText,
                onValueChange = { newCommentText = it },
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.padding(2.dp)
                    ) {
                        if (newCommentText.isEmpty()) {
                            Text(
                                text = "Add a comment...",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                },
                textStyle = LocalTextStyle.current.copy(color = Color.Gray, fontSize = 14.sp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    viewModel.addComment(post.postId, 0L, newCommentText)
                    newCommentText = ""
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                ),
                modifier = Modifier
                    .height(30.dp)
            ) {
                Text(
                    text = "Add",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Blue
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSecondCommentCard(
    post : Post,
    viewModel: PostViewModel
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painterLoginUser = rememberImagePainter(
                data = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                builder = {}
            )
            Image ( //loginUser image
                painter = painterLoginUser,
                contentDescription = "Profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(28.dp)
                    .width(28.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(10.dp))
            var newSecondCommentText by remember { mutableStateOf("") }
            val focusManager = LocalFocusManager.current

            BasicTextField(
                modifier = Modifier
                    .weight(3f)
                    .border(
                        BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(2.dp)
                /*.background(
                    color = if (viewModel.replyToUsername.value == "") Color.White else Color.LightGray
                )*/,
                value = newSecondCommentText,
                onValueChange = { newSecondCommentText = it },
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.padding(3.dp)
                    ) {
                        if (newSecondCommentText.isEmpty()) {
                            Text(
                                text = "Replying to ${viewModel.replyToUsername.value}",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                        innerTextField()
                    }
                },
                textStyle = LocalTextStyle.current.copy(color = Color.Gray, fontSize = 12.sp)
            )

            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    viewModel.addComment(post.postId, viewModel.parentCommentId.value, newSecondCommentText)
                    newSecondCommentText = ""
                    viewModel.replyToUsername.value = ""
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                ),
                modifier = Modifier
                    .height(30.dp)
                    .width(60.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Add",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Blue
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommentCard(
    post : Post,
    comment: Comment,
    navigator: DestinationsNavigator,
    viewModel: PostViewModel
){

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painterCommentProfile = rememberImagePainter(
                    data = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                    builder = {}
                )
                Image(
                    painter = painterCommentProfile ,
                    contentDescription = "Profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                        .clip(CircleShape),
                )
                Column (
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 0.dp),
                ) {
                    Text(
                        text = comment.authorUsername,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = comment.text,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(3.dp))


                    ClickableText(
                        text = AnnotatedString(
                            text = "Reply"
                        ),
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        ),
                        onClick = {
                            viewModel.replyToUsername.value = comment.authorUsername
                            viewModel.parentCommentId.value = comment.id
                        }
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (comment.canDelete) {
                    IconButton(onClick = {
                        viewModel.deleteCommentById(comment.id, post.postId)
                    }, modifier = Modifier.size(20.dp).padding(end = 5.dp)
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "",
                            tint = Color.DarkGray
                        )
                    }
                }
            }
        }
    }

    //Divider(color = Color.LightGray, modifier = Modifier.fillMaxWidth().width(1.dp))

    comment.subCommentList.forEach {
        subComment -> SubCommentCard(post = post, comment = comment, subComment = subComment, navigator = navigator, viewModel = viewModel)
    }

    if (viewModel.parentCommentId.value == comment.id) {
        AddSecondCommentCard(post = post, viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubCommentCard(
    post : Post,
    comment: Comment,
    subComment: Comment,
    navigator: DestinationsNavigator,
    viewModel: PostViewModel
){
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 10.dp, top = 0.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painterSubcommentProfile = rememberImagePainter(
                    data = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                    builder = {}
                )
                Image(
                    painter = painterSubcommentProfile,
                    contentDescription = "Profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(28.dp)
                        .width(28.dp)
                        .clip(CircleShape),
                )
                Column (
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 0.dp),
                ) {
                    Text(
                        text = subComment.authorUsername,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subComment.text,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(3.dp))

                    ClickableText(
                        text = AnnotatedString(
                            text = "Reply"
                        ),
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        ),
                        onClick = {
                            viewModel.parentCommentId.value = comment.id
                            viewModel.replyToUsername.value = subComment.authorUsername
                        }
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (subComment.canDelete) {
                    IconButton(onClick = {
                        viewModel.deleteCommentById(subComment.id, post.postId)
                    }, modifier = Modifier.size(19.dp).padding(end = 5.dp)
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "",
                            tint = Color.DarkGray
                        )
                    }
                }
            }
        }
    }

    //Divider(color = Color.LightGray, modifier = Modifier.fillMaxWidth().width(1.dp))
}