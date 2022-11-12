package com.example.frontend.presentation.location

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.common.Resource
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.use_case.get_locations.GetAllLocationsUseCase
import com.example.frontend.domain.use_case.get_locations.GetLocationsKeywordUseCase
import com.example.frontend.presentation.location.components.LocationState
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val allLocationsUseCase: GetAllLocationsUseCase,
    private val getLocationsKeywordUseCase: GetLocationsKeywordUseCase,
    private var application: Application
) : ViewModel(){

    private val _state = mutableStateOf(LocationState())
    val state : State<LocationState> = _state
    val context = application.baseContext

    init {
        getAllLocations()
    }

    fun getAllLocations()
    {
        Log.d("Locations", "Get all locations called");
        GlobalScope.launch(Dispatchers.IO) {

            var access_token =  DataStoreManager.getStringValue(context, "access_token");
            var refresh_token = DataStoreManager.getStringValue(context, "refresh_token");

            allLocationsUseCase("Bearer " + access_token).onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = LocationState(locations = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _state.value = LocationState(error = result.message ?:
                        "An unexpected error occured")
                    }
                    is Resource.Loading -> {
                        _state.value = LocationState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun searchLocations(keyword : String)
    {
        Log.d("Locations", "Search locations by ${keyword}");
        GlobalScope.launch(Dispatchers.IO) {

            var access_token =  DataStoreManager.getStringValue(context, "access_token");
            var refresh_token = DataStoreManager.getStringValue(context, "refresh_token");
            getLocationsKeywordUseCase("Bearer " + access_token, keyword).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = LocationState(locations = result.data ?: emptyList())
                        Log.d("Search back", result.data.toString())
                    }
                    is Resource.Error -> {
                        _state.value = LocationState(
                            error = result.message ?: "An unexpected error occured"
                        )
                        Log.d("Search back", "Error")
                    }
                    is Resource.Loading -> {
                        _state.value = LocationState(isLoading = true)
                        Log.d("Search back", "Loading")
                    }
                }
            }
        }
    }


}