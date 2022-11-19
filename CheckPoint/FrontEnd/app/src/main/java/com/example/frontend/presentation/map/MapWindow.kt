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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
//    userId : Long,
    viewModel: MapViewModel = hiltViewModel()
)
{
    val scaffoldState = rememberScaffoldState()
    val uiSettings = remember{
        MapUiSettings(zoomControlsEnabled = false)
    }

    val builder = LatLngBounds.Builder()


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
                .fillMaxWidth(),
            properties = viewModel.state.value.properties,
            uiSettings = uiSettings,
            onMapLoaded = {
                //load all locations for user!
            }
        ){
            Log.d("MAP", viewModel.statePosts.value.toString());
            if(viewModel.statePosts.value.userPosts != null){
                viewModel.statePosts.value.userPosts!!.forEach { post ->

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
            }

            Log.d("BUILDER", builder.toString());
        }
    }
}