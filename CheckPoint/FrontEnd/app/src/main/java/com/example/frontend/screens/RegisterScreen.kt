package com.example.frontend.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.Screen
import com.example.frontend.api.Requests


@Composable
fun RegisterScreen(navController : NavController){
    var emailFocusRequester = FocusRequester()
    var passwordFocusRequester = FocusRequester()
    var passwordRepeatFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current;

    Column(
        Modifier
            .navigationBarsPadding()
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            Modifier.size(150.dp),
            tint = Color.Black
        )

        var usernameValue = "";
        TextInput(InputType.Name,
            keyboardActions = KeyboardActions(onNext = {
                emailFocusRequester.requestFocus()
            }),
            valuePar = usernameValue, onChange = {usernameValue = it})

        var mailValue = "";
        TextInput(InputType.Mail,
            keyboardActions = KeyboardActions(onNext = {
                passwordFocusRequester.requestFocus()
            }), focusRequester = emailFocusRequester,
            valuePar = mailValue, onChange = {mailValue = it})

        var passwordValue = "";
        TextInput(InputType.Password,
            keyboardActions = KeyboardActions(onDone = {
                passwordRepeatFocusRequester.requestFocus()
            }), focusRequester = passwordFocusRequester,
            valuePar = passwordValue, onChange = {passwordValue = it})

        var passwordConfirmValue = "";
        TextInput(InputType.PasswordConfirm,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }), focusRequester = passwordRepeatFocusRequester,
            valuePar = passwordConfirmValue, onChange = {passwordConfirmValue = it})

        Button(
            onClick = {
                registerUser(context, navController,mailValue, usernameValue, passwordValue, passwordConfirmValue);
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White
            )
        ){
            Text("REGISTER", Modifier.padding(8.dp))
        }

        Divider(color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 48.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account?", color = Color.Black)
            TextButton(onClick = {
                navController.navigate(Screen.LoginScreen.route);
            }) {
                Text("LOGIN");
            }
        }
    }
}

fun registerUser(context : Context, navController: NavController,
                mail: String, username : String, password: String, passwordConfirm : String) {
    if(mail.trim()=="" || username.trim()=="" || password.trim()=="" || passwordConfirm.trim()==""){
        Toast.makeText(
            context,
            "You must fill all fields",
            Toast.LENGTH_LONG
        ).show();
        return;
    }

    Requests.register(mail.trim(), username.trim(), password.trim(), passwordConfirm.trim(), navController, context);
}