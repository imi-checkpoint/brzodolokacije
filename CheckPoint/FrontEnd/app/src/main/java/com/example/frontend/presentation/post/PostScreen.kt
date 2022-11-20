package com.example.frontend.presentation.post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.domain.model.Photo
import com.example.frontend.domain.model.Post
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
    navController: NavController
)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(photos != null && photos.size != 0)
        {
            val photo = photos.first();

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
}

@Composable
fun PostDescription(
    post : Post,
    navController: NavController
){
    Text(
        text = post.location.name,
        modifier = Modifier.size(20.dp)
    );
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