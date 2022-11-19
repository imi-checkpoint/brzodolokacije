package com.example.frontend.presentation.newpost

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NovPostScreen(navController:NavController){
    val context = LocalContext.current
    //val state = viewModel.state.value
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }
    val choseImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if(Build.VERSION.SDK_INT < 29){
            result.value = MediaStore.Images.Media.getBitmap(context.contentResolver,it)
        }
        else {
            val source = ImageDecoder.createSource(context.contentResolver,it as Uri)
            result.value = ImageDecoder.decodeBitmap(source)
        }
    }
    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(bitmap = result.value.asImageBitmap(),"",Modifier.fillMaxWidth())
        var description = ""
        TextField(value = description, onValueChange = {description=it})
        var location = ""
        TextField(value = location, onValueChange = {location=it})

        Button(onClick = { choseImage.launch("image/*") }) {
            Text("Add picture")
        }
        Button(onClick = { choseImage.launch("image/*") }) {
            Text("Post")
        }
    }

}