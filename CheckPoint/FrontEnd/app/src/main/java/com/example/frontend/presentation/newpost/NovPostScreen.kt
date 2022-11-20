package com.example.frontend.presentation.newpost

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.presentation.TextInput
import com.example.frontend.presentation.posts.PostViewModel
import okio.source
import java.io.File

@Composable
fun NovPostScreen(navController:NavController,
                  viewModel : NovPostViewModel = hiltViewModel()){
    val context = LocalContext.current
    //val state = viewModel.state.value
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }
    var lista = remember { mutableStateOf<List<Bitmap>>(emptyList())}
    var listaFiles = remember { mutableStateOf<List<File>>(emptyList())}
    val choseImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if(Build.VERSION.SDK_INT < 29){
            result.value = MediaStore.Images.Media.getBitmap(context.contentResolver,it)
            lista.value = lista.value+result.value

            //listaFiles.value = listaFiles.value + it!!.toFile()
        }
        else {
            val source = ImageDecoder.createSource(context.contentResolver,it as Uri)

            result.value = ImageDecoder.decodeBitmap(source)

            lista.value = lista.value+result.value
            //listaFiles.value = listaFiles.value +File(it.path)
        }
    }
    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(){
            items(lista.value){
                item->
                    slika(navController = navController, photo = item)
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
        Button(onClick = { viewModel.savePost(navController,description.value,location.value.toLong(),
            lista.value) }) {
            Text("Post")
        }
    }
}
@Composable
fun slika(
    navController: NavController,
    photo:Bitmap
){
    Row(){
        Image(bitmap = photo.asImageBitmap(),"")
    }
}

