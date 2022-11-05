package com.example.frontend.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
//    Text("Hello world");
    var mList: List<LocationDTO> by remember {  mutableStateOf (listOf()) }

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(18.dp, alignment = Alignment.Top),
//        horizontalAlignment = Alignment.CenterHorizontally
    ){
        search(mList = mList);

        Divider(
            color = Color.DarkGray,
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 30.dp)
        )

        lista(mList = mList);
    }

    Requests.getAll(object : CustomCallback {
        override fun onSucess(locList: List<LocationDTO>) {
            Log.d("CALLBACK", locList.toString());

//            locationList = locList;
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


@Composable
fun search(
    mList : List<LocationDTO>
) {
    var list by remember {
        mutableStateOf(mList);
    }

    var searchText by remember{ mutableStateOf("")}
//    val focusManager = LocalFocusManager.current
//    var locationList:List<LocationDTO> = emptyList()


    OutlinedTextField(
        value = searchText,
        leadingIcon = {
                      Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
        },
        onValueChange = {
                searchText = it
                Requests.search( it , object : CustomCallback {
                    override fun onSucess(locList: List<LocationDTO>) {
                        list = locList;
                    }

                    override fun onFailure() {}
                })
            },
        modifier = Modifier.fillMaxWidth(),
    )
}
