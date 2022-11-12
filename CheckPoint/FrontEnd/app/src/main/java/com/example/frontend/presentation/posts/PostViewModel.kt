package com.example.frontend.presentation.posts

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.common.Resource
import Constants.Companion.LOCATION_ID
import android.util.Log
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.use_case.location_posts.GetAllPostsForLocationUseCase
import com.example.frontend.presentation.posts.components.PostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllPostsForLocationUseCase : GetAllPostsForLocationUseCase,
    savedStateHandle: SavedStateHandle,
    application: Application
) : ViewModel(){

    private val _state = mutableStateOf(PostState())
    val state : State<PostState> = _state
    val context = application.baseContext

    init {
        savedStateHandle.get<Long>(LOCATION_ID)?.let { locationId ->
            Log.d("Location id", locationId.toString())
            getAllPostsForLocation(locationId)
        }
    }

    fun getAllPostsForLocation(locationId : Long)
    {
        GlobalScope.launch(Dispatchers.IO){
            var access_token =  DataStoreManager.getStringValue(context, "access_token");
            var refresh_token = DataStoreManager.getStringValue(context, "refresh_token");

            getAllPostsForLocationUseCase("Bearer "+access_token, locationId).onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = PostState(posts = result.data)
                    }
                    is Resource.Error -> {
                        _state.value = PostState(error = result.message ?:
                        "An unexpected error occured")
                    }
                    is Resource.Loading -> {
                        _state.value = PostState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}