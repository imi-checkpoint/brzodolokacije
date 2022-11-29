package com.example.frontend.presentation.profile_settings

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.presentation.profile_settings.components.ChangeProfilePictureState
import com.example.frontend.presentation.profile_settings.components.ProfilePictureState
import com.example.frontend.presentation.profile_settings.components.ProfileSettingsUserState
import com.example.frontend.presentation.profile_settings.components.UserInfoChangeState
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileSettingsScreen(
    navController: NavController,
    viewModel: ProfileSettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.value
    val stateEmailChange = viewModel.stateEmailChange.value
    val stateGetMyProfilePicture = viewModel.stateGetMyProfilePicture.value
    val stateChangeMyProfilePicture = viewModel.stateChangeProfilePicture.value


    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }

    val choseImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null)
        {
            if(Build.VERSION.SDK_INT < 29){
                result.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                viewModel.parsePhoto(result.value)
            }
            else {
                val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
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
            navController.popBackStack()
        }) {
            androidx.compose.material.Icon(
                Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.DarkGray
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else if(state.error != ""){
            Text("An error occured while loading user info!");
        }
        else{
            ProfilePicture(navController, viewModel, stateGetMyProfilePicture, stateChangeMyProfilePicture, choseImage, result)
            UsernameAndEmail(state, stateEmailChange, viewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfilePicture(
    navController: NavController,
    viewModel: ProfileSettingsViewModel,
    stateGetMyProfilePicture: ProfilePictureState,
    stateChangeMyProfilePicture: ChangeProfilePictureState,
    choseImage: ManagedActivityResultLauncher<String, Uri?>,
    result: MutableState<Bitmap>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 80.dp, bottom = 20.dp)
    ) {

        if(stateGetMyProfilePicture.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
        }
        else if(stateGetMyProfilePicture.error != ""){
            Text("An error occured while loading user profile picture!");
        }
        else {
            var picture = stateGetMyProfilePicture.picture
            //viewModel.currentPicture = picture;
            //println(picture.toByteArray().size)
            val decoder = Base64.getDecoder()
            val photoInBytes = decoder.decode(picture)
            if(photoInBytes.size > 1) {
                val mapa: Bitmap = BitmapFactory.decodeByteArray(photoInBytes,0, photoInBytes.size)
                //print(mapa.byteCount)
                if(mapa != null) {
                    Image(bitmap = if(viewModel.changePictureEnabled) result.value.asImageBitmap() else mapa.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier
                            .size(150.dp)
                            .weight(3f)
                            .align(Alignment.CenterVertically)
                    )

                    Column(

                    ) {

                        IconButton(onClick = {
                            choseImage.launch("image/*")
                        }, modifier = Modifier
                            .border(0.dp, Color.Gray, RectangleShape)
                            .size(30.dp)
                        ) {
                            Icon(
                                Icons.Filled.ModeEdit,
                                contentDescription = "",
                                tint = Color.DarkGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(70.dp))

                        if (viewModel.changePictureEnabled) {
                            Button(onClick = {
                                viewModel.changeProfilePicture(navController)
                            },
                                enabled = viewModel.changePictureEnabled,
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(60.dp)
                            ) {
                                Text(
                                    text = "Save",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UsernameAndEmail(
    state : ProfileSettingsUserState,
    stateEmailChange: UserInfoChangeState,
    viewModel: ProfileSettingsViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        Text(
            text = "${state.user?.username}",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(50.dp))

        /*Text(
            text = "${state.user?.email}",
            color = Color.DarkGray,
        )*/

        Spacer(modifier = Modifier.height(25.dp))

        /*var newEmail
        TextField(value = state.user!!.email, onValueChange = {location.value=it})*/
        /*var newEmail = remember {
            mutableStateOf("")
        }
        TextField(value = description.value,
            onValueChange = {description.value= it},
            label = { androidx.compose.material.Text("Description") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )*/

        IconButton(onClick = {
            //viewModel.changeEmail()
        }) {
            Icon(
                Icons.Outlined.ChangeCircle,
                contentDescription = "",
                tint = Color.DarkGray
            )
        }
    }
}

