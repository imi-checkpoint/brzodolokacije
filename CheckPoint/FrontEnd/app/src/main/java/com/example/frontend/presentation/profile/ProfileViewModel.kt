package com.example.frontend.presentation.profile

import Constants.Companion.USER_ID
import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.common.Resource
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.use_case.get_profile_data.GetMyProfileDataUseCase
import com.example.frontend.domain.use_case.get_profile_data.GetUserProfileDataUseCase
import com.example.frontend.presentation.location.components.LocationState
import com.example.frontend.presentation.profile.components.ProfileDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyProfileDataUseCase: GetMyProfileDataUseCase,
    private val getUserProfileDataUseCase: GetUserProfileDataUseCase,
    private val savedStateHandle: SavedStateHandle,
    private var application: Application
) : ViewModel(){
    private val _state = mutableStateOf(ProfileDataState())
    val state : State<ProfileDataState> = _state
    val context = application.baseContext
    var savedUserId = 0L;

    var access_token  = "";
    var refresh_token = "";

    init {
        GlobalScope.launch(Dispatchers.IO){
            access_token =  DataStoreManager.getStringValue(context, "access_token");
            refresh_token = DataStoreManager.getStringValue(context, "refresh_token");

            getProfileData()
        }
    }

    fun getProfileData()
    {
        savedStateHandle.get<Long>(USER_ID)?.let { userId ->
            if(userId == 0L) {
                getMyProfileData();
            }
            else {
                savedUserId = userId;
                getUserProfileData(userId)
            }
        }
    }

    private fun getMyProfileData()
    {
        getMyProfileDataUseCase("Bearer "+refresh_token).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ProfileDataState(profileData = result.data ?: null)
                }
                is Resource.Error -> {
                    _state.value = ProfileDataState(error = result.message ?:
                    "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = ProfileDataState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getUserProfileData(userId : Long)
    {
        getUserProfileDataUseCase("Bearer "+refresh_token, userId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ProfileDataState(profileData = result.data ?: null)
                }
                is Resource.Error -> {
                    _state.value = ProfileDataState(error = result.message ?:
                    "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = ProfileDataState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}