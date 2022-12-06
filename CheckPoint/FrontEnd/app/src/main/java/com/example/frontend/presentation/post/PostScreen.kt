package com.example.frontend.presentation.post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.lerp
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
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

    if(state.error.contains("403")){
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
            PostDetails(post = state.post, comments = stateGetComments.comments, navigator = navigator)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostDetails(
    post: Post,
    comments: List<Comment>?,
    navigator: DestinationsNavigator
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){

        //photo slider
        if(post.photos.size > 0)
            ImagePagerSlider(post.photos);

        Spacer(modifier = Modifier.height(20.dp))

        PostDescription(post, navigator);
        Spacer(modifier = Modifier.height(20.dp))

        PostMap(post, navigator)
        Spacer(modifier = Modifier.height(20.dp))

        PostComments(post, comments, navigator)
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalPagerApi
@Composable
fun ImagePagerSlider(
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
                    .padding(0.dp, 40.dp, 0.dp, 40.dp)
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
                    shape = RoundedCornerShape(20.dp)
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
                .padding(16.dp)
        )
    }
}


@Composable
fun PostDescription(
    post : Post,
    navigator: DestinationsNavigator
){

    Row(
        modifier = Modifier
            .fillMaxWidth(),
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
            Icon(
                Icons.Default.Person,
                contentDescription = "",
                tint = Color.DarkGray,
            )
            Text(
                text = post.appUserUsername,
                style = MaterialTheme.typography.bodyLarge
            );
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "",
                tint = Color.DarkGray
            )
            Text(
                text = post.location.name,
                style = MaterialTheme.typography.bodyLarge
            );
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = post.description,
        color = Color.Gray,
        style = MaterialTheme.typography.bodyMedium
    );
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
    navigator: DestinationsNavigator
){

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        backgroundColor = Color.White
    ) {
        if (comments.isNullOrEmpty()) {
            //samo forma za dodavanje komentara
        }
        else {
            LazyColumn{
                items(comments){
                        comment -> Comment(post, comment, navigator)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Comment(
    post : Post,
    comment: Comment,
    navigator: DestinationsNavigator
){

    /*Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {*/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val painter = rememberImagePainter(
                data = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                builder = {}
            )
            Image(
                painter = painter,
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
                        println("reply")
                    }
                )
            }
        }
    //}

    //Divider(color = Color.LightGray, modifier = Modifier.fillMaxWidth().width(1.dp))

    comment.subCommentList.forEach {
        subComment -> SubComment(post = post, comment = comment, subComment = subComment, navigator = navigator)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubComment(
    post : Post,
    comment: Comment,
    subComment: Comment,
    navigator: DestinationsNavigator
){
    /*Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 70.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
    ) {*/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 0.dp, top = 0.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val painter = rememberImagePainter(
                data = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                builder = {}
            )
            Image(
                painter = painter,
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
                        println("reply")
                    }
                )
            }
        }
    //}

    //Divider(color = Color.LightGray, modifier = Modifier.fillMaxWidth().width(1.dp))
}