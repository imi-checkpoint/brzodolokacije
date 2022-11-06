package com.example.frontend

import Navigation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FrontEnd)
        setContent {
            if(supportActionBar!=null){
                supportActionBar!!.hide()
            }

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
