package com.example.frontend.presentation.profile_settings

import android.content.Context
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.frontend.presentation.InputType
import com.example.frontend.presentation.TextInput
import com.example.frontend.presentation.destinations.ProfileScreenDestination
import com.example.frontend.presentation.destinations.UserListScreenDestination
import com.example.frontend.presentation.profile.ProfileScreen
import com.example.frontend.presentation.profile_settings.components.ChangeProfilePictureState
import com.example.frontend.presentation.profile_settings.components.ProfilePictureState
import com.example.frontend.presentation.profile_settings.components.ProfileSettingsUserState
import com.example.frontend.presentation.profile_settings.components.UserInfoChangeState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun ProfileSettingsScreen(
    navigator : DestinationsNavigator,
    viewModel: ProfileSettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.value
    val stateEmailChange = viewModel.stateEmailChange.value
    val stateGetMyProfilePicture = viewModel.stateGetMyProfilePicture.value
    val stateChangeProfilePicture = viewModel.stateChangeProfilePicture.value
    val statePasswordChange = viewModel.statePasswordChange.value

    var emailInput = remember {
        mutableStateOf("")
    }

    var emailFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    var oldPasswordFocusRequester = FocusRequester()
    var newPassword1FocusRequester = FocusRequester()
    var newPassword2FocusRequester = FocusRequester()


    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }

    val choseImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null)
        {
            if(Build.VERSION.SDK_INT < 29){
                result.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                //viewModel.imgbitmap = result.value
                viewModel.parsePhoto(result.value)
                viewModel.changeProfilePicture(navigator)
            }
            else {
                val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
                result.value = ImageDecoder.decodeBitmap(source)
                //viewModel.imgbitmap = result.value
                viewModel.parsePhoto(result.value)
                viewModel.changeProfilePicture(navigator)
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        IconButton(onClick = {
            //navigator.popBackStack();
            navigator.navigate(ProfileScreenDestination(viewModel.loginUserId)) //ne radi na back dugme na telefonu
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
        if(state.isLoading || stateGetMyProfilePicture.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else if(state.error != ""){
            Text("An error occured while loading user info!");
        }
        else{
            println("ucitao podatke korisnika")
            emailInput.value = viewModel.currentEmail
            ProfilePicture(navigator, viewModel, stateGetMyProfilePicture, stateChangeProfilePicture, choseImage, result, context)
            UsernameAndEmail(navigator, state, stateEmailChange, viewModel, emailInput, emailFocusRequester, focusManager)
            Passwords(navigator, state, stateEmailChange, viewModel, oldPasswordFocusRequester, newPassword1FocusRequester, newPassword2FocusRequester, focusManager)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfilePicture(
    navigator: DestinationsNavigator,
    viewModel: ProfileSettingsViewModel,
    stateGetMyProfilePicture: ProfilePictureState,
    stateChangeProfilePicture: ChangeProfilePictureState,
    choseImage: ManagedActivityResultLauncher<String, Uri?>,
    result: MutableState<Bitmap>,
    context: Context
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 80.dp, bottom = 20.dp)
    ) {

        var picture = stateGetMyProfilePicture.picture
        val decoder = Base64.getDecoder()
        val photoInBytes = decoder.decode(picture)

        if(photoInBytes.size > 1) {
            val mapa: Bitmap = BitmapFactory.decodeByteArray(photoInBytes,0, photoInBytes.size)
            if(mapa != null) {
                if (!viewModel.changePictureEnabled)
                    result.value = mapa;
                //println("*************usao")
                    Image(
                    //bitmap = if(viewModel.changePictureEnabled) result.value.asImageBitmap() else mapa.asImageBitmap(),
                    //bitmap = if(viewModel.flagPictureFirstShow) mapa.asImageBitmap() else result.value.asImageBitmap(),
                        bitmap = result.value.asImageBitmap(),
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        //.weight(3f)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically),
                    contentDescription = "Profile image",
                    contentScale = ContentScale.Crop
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
                }
            }
        }
    }
}

@Composable
fun UsernameAndEmail(
    navigator: DestinationsNavigator,
    state : ProfileSettingsUserState,
    stateEmailChange: UserInfoChangeState,
    viewModel: ProfileSettingsViewModel,
    emailInput: MutableState<String>,
    emailFocusRequester: FocusRequester,
    focusManager: FocusManager
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp, top = 30.dp, bottom = 10.dp)
    ) {

        Text(
            text = "${state.user?.username}",
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
            color = Color.DarkGray,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextInput(
            inputType = InputType.Mail,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }),
            valuePar = emailInput.value,
            onChange = {emailInput.value = it}
        );

        
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            viewModel.currentEmail = emailInput.value
            viewModel.changeEmail(navigator, emailInput.value)
        },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White
            ),
            modifier = Modifier
                .height(30.dp)
                .width(100.dp)
        ) {
            Text(
                text = "Update email",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun Passwords(
    navigator: DestinationsNavigator,
    state : ProfileSettingsUserState,
    stateEmailChange: UserInfoChangeState,
    viewModel: ProfileSettingsViewModel,
    oldPasswordFocusRequester: FocusRequester,
    newPassword1FocusRequester: FocusRequester,
    newPassword2FocusRequester: FocusRequester,
    focusManager: FocusManager
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var oldPasswordValue = "";
        TextInput(
            inputType = InputType.OldPassword,
            keyboardActions = KeyboardActions(
                onNext = {
                    newPassword1FocusRequester.requestFocus()
                }),
            valuePar = oldPasswordValue,
            onChange = {oldPasswordValue = it}
        )

        Spacer(modifier = Modifier.height(5.dp))

        var newPassword1Value = "";
        TextInput(
            inputType = InputType.NewPassword,
            focusRequester = newPassword1FocusRequester,
            keyboardActions = KeyboardActions(
                onDone = {
                    newPassword2FocusRequester.requestFocus()
                }),
            valuePar = newPassword1Value,
            onChange = {newPassword1Value = it}
        )

        Spacer(modifier = Modifier.height(5.dp))

        var newPassword2Value = "";
        TextInput(
            inputType = InputType.NewPasswordConfirm,
            focusRequester = newPassword2FocusRequester,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }),
            valuePar = newPassword2Value,
            onChange = {newPassword2Value = it}
        )

        Spacer(modifier = Modifier.height(5.dp))

        Button(onClick = {
            viewModel.changePassword(navigator, oldPasswordValue, newPassword1Value, newPassword2Value)
        },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White
            ),
            modifier = Modifier
                .height(30.dp)
                .width(120.dp)
        ) {
            Text(
                text = "Update password",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

