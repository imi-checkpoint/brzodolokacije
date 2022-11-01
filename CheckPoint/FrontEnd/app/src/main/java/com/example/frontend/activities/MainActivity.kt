package com.example.frontend.activities


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.frontend.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
            ) {
                LandingPage(this)
            }
        }
    }
}

@Composable
fun LandingPage(context: Context) {
    Column(
        Modifier
            .navigationBarsPadding()
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            Modifier.size(80.dp),
            tint = Color.Black
        )

        Button(
            onClick = {
                      goToLoginPage(context)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White
            )
        ){
            Text("LOGIN", Modifier.padding(8.dp))
        }
    }
}

fun goToLoginPage(context : Context) {
    Log.d("GO TO LOGIN PAGE", "Go to login user page");
    val intent = Intent(context,LoginActivity::class.java);
    context.startActivity(intent)
}

