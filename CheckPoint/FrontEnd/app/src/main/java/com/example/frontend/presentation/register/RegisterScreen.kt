package com.example.frontend.presentation.register

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.common.navigation.Screen
import com.example.frontend.presentation.InputType
import com.example.frontend.presentation.TextInput

@Composable
fun RegisterScreen(
    navController : NavController,
    viewModel: RegisterViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value;

    var emailFocusRequester = FocusRequester()
    var passwordFocusRequester = FocusRequester()
    var passwordRepeatFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            Modifier.size(150.dp),
            tint = Color.Black
        )


        var usernameValue = "";
        TextInput(
            inputType = InputType.Name,
            keyboardActions = KeyboardActions(
                onNext = {
                    emailFocusRequester.requestFocus()
                }),
            valuePar = usernameValue,
            onChange = {usernameValue = it}
        )

        var mailValue = "";
        TextInput(
            inputType = InputType.Mail,
            focusRequester = emailFocusRequester,
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFocusRequester.requestFocus()
                }),
            valuePar = mailValue,
            onChange = {mailValue = it}
        )

        var passwordValue = "";
        TextInput(
            inputType = InputType.Password,
            focusRequester = passwordFocusRequester,
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordRepeatFocusRequester.requestFocus()
                }),
            valuePar = passwordValue,
            onChange = {passwordValue = it}
        )

        var passwordRepeatValue = "";
        TextInput(
            inputType = InputType.Password,
            focusRequester = passwordRepeatFocusRequester,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }),
            valuePar = passwordRepeatValue,
            onChange = {passwordRepeatValue = it}
        )


        Button(onClick = {
            viewModel.register(mailValue , usernameValue, passwordValue, passwordRepeatValue, navController)
        },
            modifier =Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(text = "REGISTER" , Modifier.padding(8.dp))
        }


        Divider(
            color =Color.Black.copy(alpha= 0.3f),
            thickness =1.dp,
            modifier = Modifier.padding(top= 48.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account?", color = Color.Black)
            TextButton(onClick = {
                navController.navigate(Screen.LoginScreen.route){
                    popUpTo(Screen.RegisterScreen.route){
                        inclusive = true;
                    }
                }
            }) {
                Text(text = "LOGIN")
            }
        }


        if(state.error.isNotBlank()){//doslo je do greske tokom logina
            Text("Error! Please, try again!");
        }

        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

    }
}