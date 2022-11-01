package com.example.frontend.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.frontend.R
import com.example.frontend.api.Requests

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
            ) {
                Login(this);
            }
        }
    }
}

@Composable
fun Login(context : Context){
    var passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val username : String = "";
    val password : String = "";

    Column(
        Modifier
            .navigationBarsPadding()
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp, alignment = Alignment.Bottom),
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
                signInUser(context, usernameValue, passwordValue);
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
                goToRegister(context)
            }) {
                Text("SIGN UP");
            }
        }
    }
}


fun goToRegister(context : Context) {
    val intent = Intent(context,RegisterActivity::class.java);
    context.startActivity(intent)
}

fun signInUser(context: Context, name : String, password : String) {
    val loginRequest = Requests();
    Requests.login(name.trim(), password.trim(),context);
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
