package com.example.frontend.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.api.Requests
import com.example.frontend.models.LocationDTO

class SearchLocationActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        val Lista:List<LocationDTO> = Requests.search("E");
        setContent {
                lista(Lista);
        }
    }
}

@Composable
fun lista(Lista:List<LocationDTO>){
    LazyColumn{
        itemsIndexed(Lista){index: Int, item: LocationDTO ->
            Text(text = item.name)
        }
    }
}


@Composable
fun search() {
    var searchText by remember{ mutableStateOf("")}
    val focusManager = LocalFocusManager.current
    var locationList:List<LocationDTO> = emptyList()
    Column(

        Modifier
            .navigationBarsPadding()
            .padding(24.dp)
            .fillMaxSize()
    ) {
        TextField(
            value = searchText,
            label = {
                Text("Search location")
            },
            onValueChange = {
                searchText = it
                locationList = Requests.search(searchText)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.height(50.dp),
            onClick = {
                locationList = Requests.search(searchText)
        }){
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))
        locationList.forEach { loc ->
            Text(loc.name)
        }
    }
}
