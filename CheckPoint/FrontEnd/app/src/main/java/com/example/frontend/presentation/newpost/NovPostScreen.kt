package com.example.frontend.presentation.newpost

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.lazy.items
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.presentation.newpost.components.NovPostState
import com.example.frontend.presentation.newpost.components.SlikaState
import java.io.File

@Composable
fun NovPostScreen(navController:NavController,
                  viewModel : NovPostViewModel = hiltViewModel()){
    val context = LocalContext.current
    val state = viewModel.state.value
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }
    val choseImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if(Build.VERSION.SDK_INT < 29){
            result.value = MediaStore.Images.Media.getBitmap(context.contentResolver,it)

            viewModel.parsePhoto(result.value)
        }
        else {
            val source = ImageDecoder.createSource(context.contentResolver,it as Uri)
            result.value = ImageDecoder.decodeBitmap(source)
            viewModel.parsePhoto(result.value)
        }
    }
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
                tint = Color.DarkGray)
        }
    }
    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(){
            items(viewModel.givePhotos()){
                item->
                    slika(navController = navController, photo = item,viewModel)
            }
        }
        //Image(bitmap = result.value.asImageBitmap(),"",Modifier.fillMaxWidth())
        var description = remember {
            mutableStateOf("")
        }
        TextField(value = description.value,
            onValueChange = {description.value= it},
            label = {Text("Description")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )
        var location = remember {
            mutableStateOf("")
        }
        TextField(value = location.value, onValueChange = {location.value=it})

        Button(onClick = { choseImage.launch("image/*") }) {
            Text("Add picture")
        }
        Button(onClick = { viewModel.savePost(navController,description.value,location.value.toLong())
        }) {
            Text("Post")
        }
    }
}

@Composable
fun slika(
    navController: NavController,
    photo:SlikaState,
    viewModel : NovPostViewModel
){
    Row(){
        Image(bitmap = photo.slika.asImageBitmap(),"",Modifier.height(150.dp))
        IconButton(onClick = { viewModel.deletePhoto(photo.slika) }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Red,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

