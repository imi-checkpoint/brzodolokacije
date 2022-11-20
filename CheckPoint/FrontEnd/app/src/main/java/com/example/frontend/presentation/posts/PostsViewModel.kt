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
import com.example.frontend.domain.use_case.delete_post.DeletePostUseCase
import com.example.frontend.domain.use_case.location_posts.GetAllPostsForLocationUseCase
import com.example.frontend.domain.use_case.location_posts.GetPhotoByPostIdAndOrderUseCase

import com.example.frontend.presentation.posts.components.PostCardState
import com.example.frontend.presentation.posts.components.PostDeleteState
import com.example.frontend.presentation.posts.components.PostsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val getAllPostsForLocationUseCase : GetAllPostsForLocationUseCase,
    private val getPhotoByPostIdAndOrderUseCase: GetPhotoByPostIdAndOrderUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    savedStateHandle: SavedStateHandle,
    application: Application
) : ViewModel(){

    private val _state = mutableStateOf(PostsState())
    val state : State<PostsState> = _state
    val context = application.baseContext

    private val _stateDelete = mutableStateOf(PostDeleteState())
    val stateDelete : State<PostDeleteState> = _stateDelete;
    var locationIdTemp = savedStateHandle.get<Long>(LOCATION_ID);

    init {
        savedStateHandle.get<Long>(LOCATION_ID)?.let { locationId ->
            Log.d("Location id", locationId.toString())
            getAllPostsForLocation(locationId)
            locationIdTemp = locationId;
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
                        _state.value = PostsState(posts = result.data)
                        println(result.data)
                    }
                    is Resource.Error -> {
                        _state.value = PostsState(error = result.message ?:
                        "An unexpected error occured")
                    }
                    is Resource.Loading -> {
                        _state.value = PostsState(isLoading = true)
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


    fun deletePostById(postId: Long)
    {
        GlobalScope.launch(Dispatchers.IO){
            var access_token =  DataStoreManager.getStringValue(context, "access_token");
            var refresh_token = DataStoreManager.getStringValue(context, "refresh_token");

            deletePostUseCase("Bearer "+access_token, postId).onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _stateDelete.value = PostDeleteState(message = result.data ?: "")
                        getAllPostsForLocation(locationIdTemp!!)
                    }
                    is Resource.Error -> {
                        _stateDelete.value = PostDeleteState(error = result.message ?:
                        "An unexpected error occured")
                    }
                    is Resource.Loading -> {
                        _stateDelete.value = PostDeleteState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}