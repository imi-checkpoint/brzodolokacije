package com.example.frontend.presentation.profile_settings

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.presentation.profile.components.ProfileDataState
import com.example.frontend.presentation.profile_settings.components.ProfileSettingsUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    application: Application
): ViewModel() {

    private val _state = mutableStateOf(ProfileSettingsUserState())
    val state : State<ProfileSettingsUserState> = _state
    val context = application.baseContext

    var access_token  = "";
    var refresh_token = "";
    var username = "";
    var loginUserId = 0L;

    init {
        GlobalScope.launch(Dispatchers.Main){
            access_token =  DataStoreManager.getStringValue(context, "access_token");
            refresh_token = DataStoreManager.getStringValue(context, "refresh_token");
            username = DataStoreManager.getStringValue(context, "username");
            loginUserId = DataStoreManager.getLongValue(context, "userId");

            //getProfileData()
        }
    }
}