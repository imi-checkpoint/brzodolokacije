package com.example.frontend.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.frontend.R
import com.example.frontend.api.Requests

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
            ) {
                Register(this);
            }
        }
    }
}

@Composable
fun Register(context : Context){
    var passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .navigationBarsPadding()
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            Modifier.size(80.dp),
            tint = Color.Black
        )

        var usernameValue = "";
        TextInput(InputType.Name,
            keyboardActions = KeyboardActions(onNext = {
                passwordFocusRequester.requestFocus()
            }),
            valuePar = usernameValue, onChange = {usernameValue = it})

        var mailValue = "";
        TextInput(InputType.Mail,
            keyboardActions = KeyboardActions(onNext = {
                passwordFocusRequester.requestFocus()
            }),
            valuePar = mailValue, onChange = {mailValue = it})

        var passwordValue = "";
        TextInput(InputType.Password,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }), focusRequester = passwordFocusRequester,
            valuePar = passwordValue, onChange = {passwordValue = it})

        var passwordConfirmValue = "";
        TextInput(InputType.PasswordConfirm,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }), focusRequester = passwordFocusRequester,
            valuePar = passwordConfirmValue, onChange = {passwordConfirmValue = it})

        Button(
            onClick = {
                registerUser(context, mailValue, usernameValue, passwordValue, passwordConfirmValue);
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
                goToLogIn(context)
            }) {
                Text("LOGIN");
            }
        }
    }
}

fun goToLogIn(context : Context) {
    val intent = Intent(context,LoginActivity::class.java);
    context.startActivity(intent)
}

fun registerUser(context : Context,
                mail: String, username : String, password: String, passwordConfirm : String) {
    val registerRequest = Requests();
    registerRequest.register(mail, username, password, passwordConfirm, context);
}