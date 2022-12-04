package com.example.frontend.presentation.newpost

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.presentation.map.MapStyle
import com.example.frontend.presentation.map.updateCamera
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import android.location.Geocoder
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun NovPostMapScreen (
    navController: NavController,
    viewModel: NovPostMapViewModel =  hiltViewModel()
) {
    val context = LocalContext.current
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    var markerLatLng = remember {
        mutableStateOf<LatLng?>(null)
    }
    var markerPOI = remember {
        mutableStateOf<PointOfInterest?>(null)
    }

    val camPosState = rememberCameraPositionState {
    }
    val builder = LatLngBounds.Builder()
    var imeLokacije = remember {
        mutableStateOf("")
    }
    val localDensity = LocalDensity.current
    var mapWidth by remember {
        mutableStateOf(44)
    }

    var mapHeight by remember {
        mutableStateOf(20)
    }
    Column(
        Modifier.fillMaxSize()
    ) {
        Button(onClick = { navController.popBackStack() }, Modifier.wrapContentSize()) {
            Text(text = "Back")
        }
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .onGloballyPositioned { coords ->
                    mapWidth = with(localDensity) { coords.size.width }
                    mapHeight = with(localDensity) { coords.size.height }
                },
            uiSettings = uiSettings,
            onMapLoaded = {

            },
            cameraPositionState = camPosState,
            onMapClick = {
                markerLatLng.value = it
                markerPOI.value = null
                imeLokacije.value = ""
                /*var lokator = Geocoder(context)
                var lista = lokator.getFromLocation(it.latitude, it.latitude, 0)
                println(Geocoder.isPresent())
                if (lista != null && lista.isNotEmpty()) {
                    println(lista[0].featureName)
                    println(lista[0].countryName)
                } else {
                    println("Prazno")
                }*/
            },
            onPOIClick = {
                markerLatLng.value = null
                markerPOI.value = it
                imeLokacije.value = it.name
            },
        ) {
            if (markerLatLng.value != null) {
                Marker(position = markerLatLng.value!!, title = imeLokacije.value)
            }
            if (markerPOI.value != null) {
                Marker(position = markerPOI.value!!.latLng, title = markerPOI.value!!.name)
            }

        }
        Row(Modifier.fillMaxWidth().height(50.dp)) {
            if (markerLatLng.value != null) {
                TextField(
                    value = imeLokacije.value, onValueChange = {
                        imeLokacije.value = it
                    },
                    singleLine = true,
                )
            }
            Button(onClick = {
                if(markerLatLng.value != null) {
                    viewModel.saveLocation(imeLokacije.value,markerLatLng.value!!,navController)
                }
                else{
                    viewModel.saveLocation(markerPOI.value!!.name,markerPOI.value!!.latLng,navController)
                }
            }, Modifier.wrapContentSize()) {
                Text("Set location")
            }
        }
    }
}


