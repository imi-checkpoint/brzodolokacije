package com.example.frontend.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.Screen
import com.example.frontend.api.Requests


@Composable
fun LoginScreen(navController : NavController){
    var passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        Modifier
            .navigationBarsPadding()
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp, alignment = Alignment.Bottom),
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
                passwordFocusRequester.requestFocus()
            }), valuePar = usernameValue, onChange = {usernameValue = it})

        var passwordValue = "";
        TextInput(InputType.Password,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            focusRequester = passwordFocusRequester
            ,valuePar = passwordValue, onChange = {passwordValue = it}
        )
        Button(
            onClick = {
                signInUser( usernameValue, passwordValue, navController, context);
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White
            )
        ){
            Text("SIGN IN", Modifier.padding(8.dp))
        }

        Divider(color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 48.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?", color = Color.Black)
            TextButton(onClick = {
                navController.navigate(Screen.RegisterScreen.route);
            }) {
                Text("SIGN UP");
            }
        }
    }
}

fun signInUser( name : String, password : String, navController : NavController, context : Context) {
    Requests.login(name.trim(), password.trim(), navController, context);
}

sealed class InputType(val label:String,
                       val icon: ImageVector,
                       val keyboardOptions: KeyboardOptions,
                       val visualTransformation : VisualTransformation
){
    object Name : InputType(label = "Username",
        icon = Icons.Default.Person,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )
    object Mail : InputType(label = "Email",
        icon = Icons.Default.Email,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )
    object Password : InputType(label = "Password",
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
    object PasswordConfirm : InputType(label = "Confirm Password",
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )

}

@Composable
fun TextInput(inputType: InputType,
              focusRequester: FocusRequester?= null,
              keyboardActions: KeyboardActions,
              valuePar : String,
              onChange: (String) -> Unit = {}
)
{
    var valueChange by remember {
        mutableStateOf(valuePar)
    }
    TextField(
        value = valueChange,
        onValueChange = {valueChange = it
                        onChange(it)},
        modifier = Modifier
            .fillMaxWidth()
            .focusOrder(focusRequester ?: FocusRequester()),
        leadingIcon = { Icon(imageVector = inputType.icon, contentDescription = null)},
        label = { Text(text = inputType.label)},
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        keyboardActions = keyboardActions
    )
}
