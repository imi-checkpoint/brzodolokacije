package com.example.frontend.presentation.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.InputType
import com.example.frontend.presentation.TextInput
import com.example.frontend.presentation.destinations.LoginScreenDestination
import com.example.frontend.presentation.destinations.MainLocationScreenDestination
import com.example.frontend.presentation.destinations.RegisterScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator : DestinationsNavigator,
    viewModel : LoginViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value
    val authState = viewModel.authState.value

    if(authState.isAuthorized == true){
        navigator.navigate(MainLocationScreenDestination){
            popUpTo(LoginScreenDestination.route){
                inclusive = true;
            }
        }
    }
    if(authState.isLoading){
        Log.d("LOGIN Scr","Loading ${authState.isLoading}");
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator()
        }
    }
    else{

        var passwordFocusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        Column(
            Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Icon(
                painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                Modifier.size(150.dp),
                tint = Color.Black
            )


            var usernameValue by remember{ mutableStateOf("") }
            TextInput(
                inputType = InputType.Name,
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }),
                valuePar = usernameValue,
                onChange = {usernameValue = it}
            )

            var passwordValue by remember{ mutableStateOf("") }
            TextInput(
                inputType = InputType.Password,
                focusRequester = passwordFocusRequester,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }),
                valuePar = passwordValue,
                onChange = {passwordValue = it}
            )


            Button(onClick = {
                viewModel.login(usernameValue.trim(), passwordValue.trim());
            },
                modifier =Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Text(text = "SIGN IN" , Modifier.padding(8.dp))
            }

            Divider(
                color =Color.Black.copy(alpha= 0.3f),
                thickness =1.dp,
                modifier = Modifier.padding(top= 48.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account?", color = Color.Black)
                TextButton(onClick = {

                    navigator.navigate(RegisterScreenDestination()){
                        popUpTo(LoginScreenDestination.route){
                            inclusive = true;
                        }
                    }
                }) {
                    Text(text = "SIGN UP")
                }
            }

            if(state.error.isNotBlank()){//doslo je do greske tokom logina
                Text("Wrong credentials!");
            }

            if(state.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if(state.token != null){
                navigator.navigate(
                    MainLocationScreenDestination()
                ){
                    popUpTo(LoginScreenDestination.route){
                        inclusive = true;
                    }
                }
            }
        }

    }
}