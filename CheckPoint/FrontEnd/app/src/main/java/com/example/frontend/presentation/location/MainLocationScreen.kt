package com.example.frontend.presentation.location

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.example.frontend.common.navigation.Screen
import com.example.frontend.domain.model.Location

@Composable
fun MainLocationScreen(
    navController: NavController,
    viewModel : LocationViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value
    var searchText by remember{ mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
    {
        ProfileTopBar(navController)

        LocationSearchBar(searchText, onChange = {
            searchText = it
            if(searchText == ""){
                viewModel.getAllLocations();
            }
            else{
                viewModel.searchLocations(searchText)
            }
        })

        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else{
            LocationList(locationList = state.locations, navController = navController)
        }
    }
}


@Composable
fun LocationSearchBar(
    searchText : String,
    onChange: (String) -> Unit = {}
)
{
    var searchTextChange by remember {
        mutableStateOf(searchText)
    }

    val trailingIconView = @Composable {
        IconButton(onClick = {
            onChange("") //isprazni search text
        }) {
            Icon(
                Icons.Default.Clear,
                contentDescription ="",
                tint = Color.Black
            )
        }
    }

    TextField(
        value = searchText,
        trailingIcon = if(searchText.isNotBlank()) trailingIconView else null,
        onValueChange = {
            onChange(it)
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text("Search locations")
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),

        )
}

@Composable
fun LocationList(
    locationList : List<Location>?,
    navController: NavController
)
{
    Log.d("ALL", locationList.toString())

    if(locationList == null || locationList.isEmpty()){
        Text(
            text = "No locations found!",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }

    else{
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ){
            items(locationList){
                    location -> LocationCard(location = location, navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationCard(
    location : Location,
    navController: NavController
)
{
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation =2.dp,
        backgroundColor =Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick = {
            navController.navigate(Screen.PostsScreen.withArgs(location.id));
        }
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(location.name)
                Text(location.coordinateX.toString())
                Text(location.coordinateY.toString())
            }
        }
    }
}

@Composable
fun ProfileTopBar(
    navController: NavController
)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
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
//                go to messages
            }) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
    }
}