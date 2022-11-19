package com.example.frontend.presentation.posts

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.common.Resource
import Constants.Companion.LOCATION_ID
import android.util.Log
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.model.Post
import com.example.frontend.domain.use_case.location_posts.GetAllPostsForLocationUseCase
import com.example.frontend.domain.use_case.location_posts.GetPhotoByPostIdAndOrderUseCase

import com.example.frontend.presentation.posts.components.PostCardState
import com.example.frontend.presentation.posts.components.PostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllPostsForLocationUseCase : GetAllPostsForLocationUseCase,
    private val getPhotoByPostIdAndOrderUseCase: GetPhotoByPostIdAndOrderUseCase,
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

    fun getPhoto(post: Post): PostCardState {
        val _CardState = mutableStateOf(PostCardState())
        val state : State<PostCardState> = _CardState
        GlobalScope.launch(Dispatchers.IO){
            var access_token =  DataStoreManager.getStringValue(context, "access_token");
            var refresh_token = DataStoreManager.getStringValue(context, "refresh_token");
            println("test------")
            if(!post.photos.isEmpty())
            getPhotoByPostIdAndOrderUseCase("Bearer "+access_token, post.photos[0].postId,post.photos[0].order).map { result ->
                when(result){
                    is Resource.Success -> {
                        println("--secces")
                        _CardState.value = PostCardState(picture = result.data!!.data.toByteArray())
                    }
                    is Resource.Error -> {
                        println("--error")
                        _CardState.value = PostCardState(error = result.message ?:
                        "An unexpected error occured")
                        println(result.message.toString())
                    }
                    is Resource.Loading -> {
                        println("--loading")
                        _CardState.value = PostCardState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
        return _CardState.value;
    }

}