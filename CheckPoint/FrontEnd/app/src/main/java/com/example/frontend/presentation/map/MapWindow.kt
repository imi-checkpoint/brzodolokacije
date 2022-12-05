package com.example.frontend.presentation.map

import android.annotation.SuppressLint
import android.graphics.Camera
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.domain.model.Post
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapWindow(
    userId : Long,
    posts : List<Post>,
    viewModel: MapViewModel = hiltViewModel()
)
{
    val scaffoldState = rememberScaffoldState()
    val uiSettings = remember{
        MapUiSettings(zoomControlsEnabled = false)
    }

    val camPosState = rememberCameraPositionState{
    }
    val builder = LatLngBounds.Builder()

    val localDensity = LocalDensity.current
    var mapWidth by remember{
        mutableStateOf(0)
    }

    var mapHeight by remember{
        mutableStateOf(0)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(MapEvent.ToggleLightMap)
            }) {
                Icon(imageVector = if(viewModel.state.value.isLightMap){
                    Icons.Default.ToggleOff
                } else Icons.Default.ToggleOn,
                    contentDescription = "Toggle fallout map"
                )
            }
        }
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coords ->
                    mapWidth = with(localDensity){coords.size.width}
                    mapHeight = with(localDensity){coords.size.height}
                },
            properties = viewModel.state.value.properties,
            uiSettings = uiSettings,
            onMapLoaded = {

            },
            onMapLongClick = {
                Log.d("Long click", "Map long click");
                Log.d("LATLNG", it.toString());
            },
            cameraPositionState = camPosState
        ){

            posts.forEach { post ->

                val postLocation = LatLng(post.location.lat, post.location.lng)
                builder.include(postLocation)

                Marker(
                    position = postLocation,
                    title = post.location.name,
                    snippet = "User post location",
                    onInfoWindowClick = {},
                    onInfoWindowLongClick = {},
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }

            if(!posts.isEmpty())
            {
                updateCamera(camPosState, builder, mapWidth, mapHeight)
            }
        }
    }
}

fun updateCamera(
    cameraPositionState: CameraPositionState,
    builder : LatLngBounds.Builder,
    width: Int,
    height : Int
){
    val padding = (width * 0.20).toInt();
    cameraPositionState.move(
        update = CameraUpdateFactory.newLatLngBounds(
            builder.build(),
            width,
            height,
            padding
        )
    )
}