package com.example.frontend.activities

import Navigation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.R

import com.example.frontend.Screen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContent {
            if(supportActionBar!=null){
                supportActionBar!!.hide()
            }
//            Text("Hello");
//            Navigation()

            MyTopBar();

        }
    }
}

@Composable
fun MyTopBar()
{
    Column() {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Navigation();
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {

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
                      navController.navigate(Screen.LoginScreen.route);
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

