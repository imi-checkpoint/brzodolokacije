package com.example.frontend.presentation.location

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
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
    val context = LocalContext.current
    viewModel.getAllLocations(context)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.locations) { location ->
                LocationItem(
                    location = location,
                    onItemClick = {
                        navController.navigate(Screen.PostsScreen.route + "/${location.id}")
                    }
                )
            }
        }
        if(state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
        if(state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

//    val state = viewModel.state.value
//    var allLocations = state.locations
//    var searchText by remember{ mutableStateOf("") }
//    val context = LocalContext.current
//
//    if(searchText == ""){
//        viewModel.getAllLocations(context)
//    Log.d("STATE LOCATIONS", state.locations.toString())
//    }
//    else{
//        viewModel.searchLocations(context, searchText);
//    }
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp)
//    )
//    {
//        ProfileTopBar()
//        LocationSearchBar(searchText, onChange = {searchText = it})
//        if(state.isLoading){
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//        }
//        else{
//            LocationList(locationList = allLocations, navController = navController)
//        }
//    }
}


@Composable
fun LocationItem(
    location : Location,
    onItemClick: (Location) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(location) }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${location.id}. ${location.name} (${location.coordinateX})",
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun LocationList(
    locationList : List<Location>?,
    navController: NavController
)
{
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