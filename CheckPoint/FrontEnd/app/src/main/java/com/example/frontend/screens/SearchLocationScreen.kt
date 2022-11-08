package com.example.frontend.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.Screen
import com.example.frontend.api.CustomCallback
import com.example.frontend.api.Requests
import com.example.frontend.models.LocationDTO

@Composable
fun MainSearchScreen(navController: NavController)
{

    LocationScreen(navController = navController)
}

@Composable
fun LocationScreen(navController: NavController)
{
    val context = LocalContext.current
    var mList: List<LocationDTO> by remember {  mutableStateOf (listOf()) }
    var searchText by remember{ mutableStateOf("")}

    if(mList.isEmpty() && searchText.equals(""))
        Requests.getAll(object : CustomCallback<List<LocationDTO>> {
            override fun onSucess(locList: List<LocationDTO>) {
                mList = locList;
            }
            override fun onFailure() {}
        }, context)

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(20.dp),
    ){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        )
        {
            Row(){
                IconButton(onClick = {
                    navController.navigate(Screen.ProfileScreen.route);
                }) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }

                IconButton(onClick = {
                    /* go to my messages */
                }) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            }
        }

        val trailingIconView = @Composable {
            IconButton(
                onClick = {
                    searchText = "";
                    Requests.getAll(object : CustomCallback<List<LocationDTO>> {
                        override fun onSucess(locList: List<LocationDTO>) {
                            mList = locList;
                        }
                        override fun onFailure() {}
                    }, context)
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
                Requests.search( it , object : CustomCallback<List<LocationDTO>> {
                    override fun onSucess(locList: List<LocationDTO>) {
                        mList = locList;
                    }
                    override fun onFailure() {
                    }
                }, context)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Search locations"
                )
            }
        )

//        Divider(
//            color = Color.DarkGray,
//            thickness = 2.dp,
//            modifier = Modifier.padding(vertical = 30.dp)
//        )

        Text(text = if(mList.isEmpty())"No locations found!" else "",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight())
        lista(mList = mList,navController);
    }


}

@Composable
fun lista(
    mList : List<LocationDTO>,navController: NavController
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ){
        items(mList){
                each -> locationCard(location = each,navController)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun locationCard(location : LocationDTO,navController: NavController)
{
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick = {
            navController.currentBackStackEntry?.arguments?.putLong("location",location.id)
            navController.navigate(Screen.PostsScreen.route);
        }
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
