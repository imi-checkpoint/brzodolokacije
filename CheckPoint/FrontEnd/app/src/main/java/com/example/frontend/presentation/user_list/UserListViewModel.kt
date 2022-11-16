package com.example.frontend.presentation.user_list

import Constants.Companion.USER_ID
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
import com.example.frontend.domain.use_case.get_profile_data.*
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
    private val getUserFollowersUseCase: GetUserFollowersUseCase,
    private val getUserFollowingUseCase: GetUserFollowingUseCase,
    private val searchMyFollowersUseCase: SearchMyFollowersUseCase,
    private val searchMyFollowingUseCase: SearchMyFollowingUseCase,
    private val searchUserFollowersUseCase: SearchUserFollowersUseCase,
    private val searchUserFollowingUseCase: SearchUserFollowingUseCase,
    private val savedStateHandle: SavedStateHandle,
    application: Application
) : ViewModel(){

    private val _state = mutableStateOf(UserListState())
    val state : State<UserListState> = _state
    private val context = application.baseContext
    private var access_token = "";
    private var refresh_token = "";

    var typeOfUsers = "";
    var savedUserId : Long = 0L;

    init {
        Log.d("INIT", "Initialize");
        GlobalScope.launch(Dispatchers.IO){
            access_token =  DataStoreManager.getStringValue(context, "access_token");
            refresh_token = DataStoreManager.getStringValue(context, "refresh_token");

            savedStateHandle.get<Long>(USER_ID)?.let { userId ->
                Log.d("SAVED ID", userId.toString())
                savedUserId = userId
            }

            savedStateHandle.get<String>(USER_LIST_TYPE)?.let { userTypeList ->
                if(userTypeList == "following") {
                    typeOfUsers = "Following"
                }
                else if(userTypeList == "followers") {
                    typeOfUsers = "Followers"
                }
            }

            getAllUsers()
        }
    }


    fun getAllUsers()
    {
        Log.d("LIST","get all " )
        Log.d("TYPE", typeOfUsers)
        Log.d("User id", savedUserId.toString());
        if(typeOfUsers == "Following"){
            if(savedUserId == 0L){
                getAllMyFollowing(access_token)
            }
            else{
                Log.d("USER FOLLOWING", savedUserId.toString())
                getUsersFollowing(access_token, savedUserId);
            }
        }
        else if(typeOfUsers == "Followers"){
            if(savedUserId == 0L){
                getAllMyFollowers(access_token)
            }
            else{
                Log.d("USER FOLLOWERS", savedUserId.toString())
                getUsersFollowers(access_token, savedUserId);
            }
        }
    }



    fun searchUsers(keyword: String)
    {
        if(typeOfUsers == "Following"){
            if(savedUserId == 0L){
                searchAllMyFollowing(access_token, keyword)
            }
            else{
                searchUsersFollowing(access_token, savedUserId, keyword);
            }
        }
        else if(typeOfUsers == "Followers"){
            if(savedUserId == 0L){
                searchAllMyFollowers(access_token, keyword)
            }
            else{
                searchUsersFollowers(access_token, savedUserId, keyword);
            }
        }
    }


    private fun getAllMyFollowers(token : String)
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

    private fun getAllMyFollowing(token : String)
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

    private fun getUsersFollowers(token: String, userId : Long){
        getUserFollowersUseCase("Bearer "+token, userId).onEach { result ->
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

    private fun getUsersFollowing(token: String, userId : Long){
        getUserFollowingUseCase("Bearer "+token, userId).onEach { result ->
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

    private fun searchAllMyFollowing(token : String, keyword: String)
    {
        searchMyFollowingUseCase("Bearer "+token, keyword).onEach { result ->
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

    private fun searchAllMyFollowers(token : String, keyword: String){
        searchMyFollowersUseCase("Bearer "+token, keyword).onEach { result ->
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

    private fun searchUsersFollowing(token : String, userId : Long, keyword: String){
        searchUserFollowingUseCase("Bearer "+token, userId, keyword).onEach { result ->
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

    private fun searchUsersFollowers(token : String, userId : Long, keyword: String){
        searchUserFollowersUseCase("Bearer "+token, userId, keyword).onEach { result ->
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