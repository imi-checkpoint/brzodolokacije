package com.example.frontend.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.api.CustomCallback
import com.example.frontend.api.Requests
import com.example.frontend.models.LocationDTO

@Composable
fun MainSearchScreen(navController: NavController)
{
    //profile page bar

    LocationScreen(navController = navController)
}

@Composable
fun LocationScreen(navController: NavController)
{
    var mList: List<LocationDTO> by remember {  mutableStateOf (listOf()) }
    var searchText by remember{ mutableStateOf("")}
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(20.dp),
    ){
        val trailingIconView = @Composable {
            IconButton(
                onClick = {
                    searchText = ""
                },
            ) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
        OutlinedTextField(
            value = searchText,
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
            },
            trailingIcon = if (searchText.isNotBlank()) trailingIconView else null ,
            onValueChange = {
                searchText = it
                Requests.search( it , object : CustomCallback {
                    override fun onSucess(locList: List<LocationDTO>) {
                        mList = locList;
                    }
                    override fun onFailure() {
                    }
                })
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Divider(
            color = Color.DarkGray,
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 30.dp)
        )

        Text(text = if(mList.isEmpty())"No locations found!" else "",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight())
        lista(mList = mList);
    }
    if(mList.isEmpty() && searchText.equals(""))
    Requests.getAll(object : CustomCallback {
        override fun onSucess(locList: List<LocationDTO>) {
            mList = locList;
        }
        override fun onFailure() {}
    })
}

@Composable
fun lista(
    mList : List<LocationDTO>
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ){
        items(mList){
                each -> locationCard(location = each)
        }
    }
}

@Composable
fun locationCard(location : LocationDTO)
{
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ){
        Row {
            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = location.name)
                Text(text = location.coordinateX.toString())
                Text(text = location.coordinateY.toString())
            }
        }
    }
}