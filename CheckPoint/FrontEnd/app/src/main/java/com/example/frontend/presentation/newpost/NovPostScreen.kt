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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.domain.model.Location
import com.example.frontend.presentation.destinations.LoginScreenDestination
import com.example.frontend.presentation.destinations.MainLocationScreenDestination
import com.example.frontend.presentation.destinations.NovPostMapScreenDestination
import com.example.frontend.presentation.newpost.components.NovPostState
import com.example.frontend.presentation.newpost.components.SlikaState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.io.File

@Destination
@Composable
fun NovPostScreen(navigator: DestinationsNavigator,
                  viewModel : NovPostViewModel = hiltViewModel()){
    val context = LocalContext.current
    val state = viewModel.state.value


    if(state.error.contains("403")){
        navigator.navigate(LoginScreenDestination){
            popUpTo(MainLocationScreenDestination.route){
                inclusive = true;
            }
        }
    }

    viewModel.proveriConstants()
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }
    var expanded = remember { mutableStateOf(false) }
    var selected = remember {
        mutableStateOf(0)
    }
    val choseImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null)
        {
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
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        IconButton(onClick = {
            navigator.popBackStack()
        }
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.DarkGray)
        }
    }
    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),

        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(viewModel.givePhotos()) { item ->
                slika(navigator, photo = item, viewModel)
            }
        }
        //Image(bitmap = result.value.asImageBitmap(),"",Modifier.fillMaxWidth())
        var description = remember {
            mutableStateOf("")
        }
        TextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 5
        )

        var location = remember {
            mutableStateOf<Location>(Location(0, "", 0.0, 0.0))
        }
        Text(text = viewModel.getLocation())
        Button(onClick = { choseImage.launch("image/*") }) {
            Text("Add picture")
        }
        Button(onClick = { expanded.value = true }) {
            Text("Izaberi lokaciju")
        }
        Button(
            onClick = {
                viewModel.savePost(
                    navigator,
                    description.value,
                    location.value.id
                )
            },
            enabled = viewModel.givePhotos().isNotEmpty() && viewModel.getLocation()!=""
        ) {
            Text("Post")
        }


        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .wrapContentWidth()
                .height(300.dp),


        ) {
            viewModel.dajLokacije().forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    viewModel.setLocation(item.id)
                }
                ) {
                    Text(item.name)
                }
            }
        }
        Button(onClick = { navigator.navigate(NovPostMapScreenDestination)}) {
            Text("Izaberi lokaciju sa mape")
        }

    }
}

@Composable
fun slika(
    navigator: DestinationsNavigator,
    photo:SlikaState,
    viewModel : NovPostViewModel
){
    Row(
        Modifier
            .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(20.dp))
            .wrapContentHeight()
            .width(300.dp)
            .padding(20.dp)

    ){
        Column() {
            Image(bitmap = photo.slika.asImageBitmap(),"",
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 5.dp)
            )
            IconButton(onClick = { viewModel.deletePhoto(photo.slika) },
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(width = 0.dp, color = Color.Gray, shape = RoundedCornerShape(50.dp))
                    .background(color = Color.LightGray, shape = RoundedCornerShape(50.dp))
                    .padding(10.dp, 10.dp, 10.dp, 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

