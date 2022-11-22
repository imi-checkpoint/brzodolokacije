package com.example.frontend.presentation.map

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.common.Resource
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.use_case.get_user_posts.GetUserPostsUseCase
import com.example.frontend.presentation.map.components.MapState
import com.example.frontend.presentation.map.components.UserPostsState
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private var getUserPostUseCase : GetUserPostsUseCase,
    private var application: Application,
) : ViewModel(){

    private val _state = mutableStateOf(MapState())
    val state : State<MapState> = _state

    private val _statePosts = mutableStateOf(UserPostsState())
    val statePosts : State<UserPostsState> = _statePosts

    val context = application.baseContext
    var access_token  = "";
    var refresh_token = "";

    init {
        GlobalScope.launch(Dispatchers.IO){
            access_token =  DataStoreManager.getStringValue(context, "access_token");
            refresh_token = DataStoreManager.getStringValue(context, "refresh_token");
        }
    }

    fun getAllPostLocations(userId : Long){
        Log.d("GETTING POSTS", "Getting posts for userid ${userId.toString()}")
        getUserPostUseCase("Bearer "+refresh_token, userId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _statePosts.value = UserPostsState(userPosts = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _statePosts.value = UserPostsState(error = result.message ?:
                    "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _statePosts.value = UserPostsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event : MapEvent)
    {
        when(event){
            is MapEvent.ToggleLightMap -> {
                _state.value = _state.value.copy(
                    properties = _state.value.properties.copy(
                        mapStyleOptions = if(_state.value.isLightMap) {
                            null
                        } else MapStyleOptions(MapStyle.json),
                    ),
                    isLightMap = !_state.value.isLightMap
                )
            }
            is MapEvent.OnMapLongClick -> {

            }
            is MapEvent.OnInfoWindowLongClick -> {

            }
        }
    }



}