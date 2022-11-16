package com.example.frontend.presentation.user_list

import Constants.Companion.USER_LIST_TYPE
import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.common.Resource
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.use_case.get_profile_data.GetMyFollowersUseCase
import com.example.frontend.domain.use_case.get_profile_data.GetMyFollowingUseCase
import com.example.frontend.presentation.location.components.LocationState
import com.example.frontend.presentation.user_list.components.UserListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getMyFollowersUseCase: GetMyFollowersUseCase,
    private val getMyFollowingUseCase: GetMyFollowingUseCase,
    private val savedStateHandle: SavedStateHandle,
    application: Application
) : ViewModel(){

    private val _state = mutableStateOf(UserListState())
    val state : State<UserListState> = _state
    private val context = application.baseContext
    private var access_token = "";
    private var refresh_token = "";
    var type = "";

    init {
        Log.d("INIT", "Initialize");
        GlobalScope.launch(Dispatchers.IO){
            access_token =  DataStoreManager.getStringValue(context, "access_token");
            refresh_token = DataStoreManager.getStringValue(context, "refresh_token");

            getUsers();
        }
    }

    fun getUsers(){
        savedStateHandle.get<String>(USER_LIST_TYPE)?.let { userTypeList ->
            if(userTypeList == "following") {
                type = "Following"
                getAllFollowingForUser(access_token)
            }
            else if(userTypeList == "followers") {
                type = "Followers"
                getAllFollowersForUser(access_token)
            }
        }
    }

    fun getSearchUsers(keyword : String){
        savedStateHandle.get<String>(USER_LIST_TYPE)?.let { userTypeList ->
            if(userTypeList == "following") getAllFollowingForUser(access_token)
            else if(userTypeList == "followers") getAllFollowersForUser(access_token)
        }
    }

    private fun getAllFollowersForUser(token : String)
    {
        getMyFollowersUseCase("Bearer "+token).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = UserListState(users = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = UserListState(error = result.message ?:
                    "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = UserListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getAllFollowingForUser(token : String)
    {
        getMyFollowingUseCase("Bearer "+token).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = UserListState(users = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = UserListState(error = result.message ?:
                    "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = UserListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}