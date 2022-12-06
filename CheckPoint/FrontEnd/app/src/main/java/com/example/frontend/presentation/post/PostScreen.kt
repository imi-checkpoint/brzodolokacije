package com.example.frontend.presentation.post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
import com.example.frontend.presentation.InputType
import com.example.frontend.presentation.TextInput
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

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
            .fillMaxWidth()
    ){

        //sad se prikazuje samo prva
        PostPhotos(post.photos, navigator);
        Spacer(modifier = Modifier.height(20.dp))

        PostDescription(post, navigator);
        Spacer(modifier = Modifier.height(20.dp))

        PostMap(post, navigator)
        Spacer(modifier = Modifier.height(20.dp))

        PostComments(post, comments, navigator, viewModel)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostPhotos(
    photos : List<Photo>,
    navigator: DestinationsNavigator,
)
{

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        if(photos != null && photos.size != 0)
        {
            PhotoSlider(photos)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PhotoSlider(
    photos : List<Photo>
)
{
    var index by remember{
        mutableStateOf(0)
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        OnePhoto(photos = photos, index = index)
    }


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        IconButton(
            onClick =
            {
                index -= 1 ;
                Log.d("NEW INDEX", index.toString());
            },
            enabled = if(photos.size > 1 && index!=0) true else false
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = if(photos.size > 1 && index!=0) Color.Black else Color.Gray,
                modifier = Modifier.size(20.dp),
            )
        }

        IconButton(
            onClick =
            {
                index += 1 ;
                Log.d("NEW INDEX", index.toString());
            },
            enabled = if(photos.size > 1 && index != (photos.size - 1) ) true else false
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Forward",
                tint = if(photos.size > 1 && index != (photos.size - 1) ) Color.Black else Color.Gray,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnePhoto(
    photos : List<Photo>,
    index : Int
){
   Column(
       modifier = Modifier
           .fillMaxWidth()
           .height(250.dp),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
   ) {
       val photo = photos.get(index);

       val decoder = Base64.getDecoder()
       val photoBytes = decoder.decode(photo.photo.data)
       if(photoBytes.size>1){
           val mapa: Bitmap = BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.size)
           print(mapa.byteCount)
           if(mapa!=null){
               Image(
                   bitmap = mapa.asImageBitmap(),
                   contentDescription ="" ,
                   contentScale = ContentScale.FillHeight
               )
           }
       }
   }
}

@Composable
fun PostDescription(
    post : Post,
    navigator: DestinationsNavigator
){
    Text(text = post.location.name);
    Text(text = post.appUserUsername);
    Spacer(modifier = Modifier.height(10.dp))

    Text("Description");
    Text(text = post.description);
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

    AddCommentField(post, navigator, viewModel)

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        backgroundColor = Color.White
    ) {
        //AddCommentField(post, navigator, viewModel)

        if (!(comments.isNullOrEmpty()))
            LazyColumn{
                items(comments){
                        comment -> Comment(post, comment, navigator, viewModel)
                }
            }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddCommentField(
    post : Post,
    navigator: DestinationsNavigator,
    viewModel: PostViewModel
) {
    /*Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {*/
        /*val painterLoginUser = rememberImagePainter(
            data = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
            builder = {}
        )
        Image( //loginUser image
            painter = painterLoginUser,
            contentDescription = "Profile image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .clip(CircleShape),
        )*/
        var newCommentText by remember{ mutableStateOf("") }
        val focusManager = LocalFocusManager.current
        TextInput(
            inputType = InputType.NewComment,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }),
            valuePar = newCommentText,
            onChange = {newCommentText= it}
        )
        /*ClickableText(
            text = AnnotatedString(
                text = "Post"
            ),
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            ),
            onClick = {
                viewModel.addComment(post.postId, viewModel.parentCommentId, newCommentText)
            }
        )*/
        Button(onClick = {
            println(post.postId.toString() + " " + viewModel.parentCommentId + " " + newCommentText)
            viewModel.addComment(post.postId, viewModel.parentCommentId, newCommentText)
            newCommentText = ""
        },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White
            ),
            modifier = Modifier
                .height(38.dp)
                .width(110.dp)
        ) {
            Text(
                text = "Add comment",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }


    //}
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Comment(
    post : Post,
    comment: Comment,
    navigator: DestinationsNavigator,
    viewModel: PostViewModel
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
                        viewModel.parentCommentId = 0L
                    }
                )
            }
        }
    //}

    //Divider(color = Color.LightGray, modifier = Modifier.fillMaxWidth().width(1.dp))

    comment.subCommentList.forEach {
        subComment -> SubComment(post = post, comment = comment, subComment = subComment, navigator = navigator, viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubComment(
    post : Post,
    comment: Comment,
    subComment: Comment,
    navigator: DestinationsNavigator,
    viewModel: PostViewModel
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
                        viewModel.parentCommentId = comment.id
                    }
                )
            }
        }
    //}

    //Divider(color = Color.LightGray, modifier = Modifier.fillMaxWidth().width(1.dp))
}