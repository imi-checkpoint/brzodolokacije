package com.example.frontend.presentation.map

import android.annotation.SuppressLint
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
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapWindow(
    viewModel: MapViewModel = hiltViewModel()
)
{
    val scaffoldState = rememberScaffoldState()
    val uiSettings = remember{
        MapUiSettings(zoomControlsEnabled = false)
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
            modifier = Modifier.fillMaxWidth(),
            properties = viewModel.state.value.properties,
            uiSettings = uiSettings,
            onMapLoaded = {
                //load all locations for user!
            }
        )
    }

}