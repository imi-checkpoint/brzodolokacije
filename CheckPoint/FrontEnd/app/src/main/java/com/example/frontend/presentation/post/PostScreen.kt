package com.example.frontend.presentation.post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.domain.model.Photo
import com.example.frontend.domain.model.Post
import com.google.accompanist.pager.ExperimentalPagerApi
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostScreen(
    navController: NavController,
    viewModel: PostViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        if(state.isLoading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        else if(state.post!=null){
            PostDetails(post = state.post, navController = navController)
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostDetails(
    post: Post,
    navController: NavController
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){

        //sad se prikazuje samo prva
        PostPhotos(post.photos, navController);
        Spacer(modifier = Modifier.height(20.dp))

        PostDescription(post, navController);
        Spacer(modifier = Modifier.height(20.dp))

        PostMap(post, navController)
        Spacer(modifier = Modifier.height(20.dp))
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostPhotos(
    photos : List<Photo>,
    navController: NavController,
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
    navController: NavController
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
    navController : NavController
){
    Text("POST MAP")
}

