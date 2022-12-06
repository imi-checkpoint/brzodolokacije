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
import com.example.frontend.domain.use_case.follow_or_unfollow.FollowUnfollowUseCase
import com.example.frontend.domain.use_case.get_profile_data.GetUserProfileDataUseCase
import com.example.frontend.domain.use_case.get_profile_data.GetUserProfilePhotoUseCase
import com.example.frontend.domain.use_case.get_user_posts.GetUserPostsUseCase
import com.example.frontend.domain.use_case.refresh_page.RefreshPageUseCase
import com.example.frontend.presentation.newpost.components.NovPostState
import com.example.frontend.presentation.profile.components.UserPostsState
import com.example.frontend.presentation.profile.components.ProfileDataState
import com.example.frontend.presentation.profile.components.ProfilePictureState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileDataUseCase: GetUserProfileDataUseCase,
    private val getUserProfilePhotoUseCase: GetUserProfilePhotoUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val followUnfollowUseCase : FollowUnfollowUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val refreshPageUseCase : RefreshPageUseCase,
    private var application: Application
) : ViewModel(){
    private val _state = mutableStateOf(ProfileDataState())
    val state : State<ProfileDataState> = _state

    private val _pictureState = mutableStateOf(ProfilePictureState())
    val pictureState : State<ProfilePictureState> = _pictureState

    private val _postsState = mutableStateOf(UserPostsState())
    val postsState : State<UserPostsState> = _postsState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> get() = _isRefreshing.asStateFlow()


    val context = application.baseContext
    var savedUserId = 0L;

    var access_token  = "";
    var refresh_token = "";
    var username = "";
    var loginUserId = 0L;

    init {
        GlobalScope.launch(Dispatchers.Main){
            access_token =  DataStoreManager.getStringValue(context, "access_token").trim();
            refresh_token = DataStoreManager.getStringValue(context, "refresh_token").trim();
            username = DataStoreManager.getStringValue(context, "username");
            loginUserId = DataStoreManager.getLongValue(context, "userId");

            Log.d("PROFILE", "Token is *${refresh_token}*");

            getProfileData()
        }
    }

    fun getProfileData()
    {
        savedStateHandle.get<Long>(USER_ID)?.let { userId ->
            Log.d("PROFILE USERID IS", userId.toString())
            savedUserId = userId;
            getUserProfileData(userId);
            getUserPhoto(userId);
            getUserPosts(userId);
        }
    }

    private fun getUserProfileData(userId : Long)
    {
        getUserProfileDataUseCase("Bearer "+refresh_token, userId, loginUserId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    Log.d("PROFILE", "Recieved profile data for userId ${userId.toString()}");
                    Log.d("PROFILE", result.data.toString());
                    _state.value = ProfileDataState(profileData = result.data ?: null)
                }
                is Resource.Error -> {
                    Log.d("PROFILE", "Error getting profile data");
                    _state.value = ProfileDataState(error = result.message ?:
                    "An unexpected error occured")

                    if(result.message?.contains("403") == true){
                        GlobalScope.launch(Dispatchers.Main){
                            DataStoreManager.deleteAllPreferences(context);
                        }
                    }
                }
                is Resource.Loading -> {
                    _state.value = ProfileDataState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getUserPhoto(userId: Long){
        getUserProfilePhotoUseCase("Bearer "+refresh_token, userId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    Log.d("PROF PICTURE", "got");
                    _pictureState.value = ProfilePictureState(profilePicture = result.data ?: "")
                }
                is Resource.Error -> {
                    Log.d("PROFILE", "Error getting profile picture");
                    _pictureState.value = ProfilePictureState(error = result.message ?:
                    "An unexpected error occured")

                    if(result.message?.contains("403") == true){
                        GlobalScope.launch(Dispatchers.Main){
                            DataStoreManager.deleteAllPreferences(context);
                        }
                    }
                }
                is Resource.Loading -> {
                    _pictureState.value = ProfilePictureState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun followUnfollowUser(){
        followUnfollowUseCase("Bearer "+refresh_token, savedUserId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    this.getUserProfileData(savedUserId);
                    //da se na prethodnoj strani refreshuje state
                    Constants.refreshFollowUnfollowConstant = savedUserId
                    Constants.refreshProfileConstant = savedUserId
                }
                is Resource.Error -> {
                    _state.value = ProfileDataState(error = result.message ?:
                    "An unexpected error occured")

                    if(result.message?.contains("403") == true){
                        GlobalScope.launch(Dispatchers.Main){
                            DataStoreManager.deleteAllPreferences(context);
                        }
                    }
                }
                is Resource.Loading -> {
                    _state.value = ProfileDataState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getUserPosts(userId : Long){
        getUserPostsUseCase("Bearer "+refresh_token, userId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _postsState.value = UserPostsState(userPosts = result.data ?: null)
                }
                is Resource.Error -> {
                    Log.d("PROFILE", "Error getting user posts");
                    _postsState.value = UserPostsState(error = result.message ?:
                    "An unexpected error occured")

                    if(result.message?.contains("403") == true){
                        GlobalScope.launch(Dispatchers.Main){
                            DataStoreManager.deleteAllPreferences(context);
                        }
                    }
                }
                is Resource.Loading -> {
                    _postsState.value = UserPostsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun refreshPage(){
        refreshPageUseCase().onEach{ result ->
            _isRefreshing.emit(result)
        }.launchIn(viewModelScope)
    }

    fun proveriConstants(){
        Log.d("PROFILE", "Proveri constants");
        if(this.loginUserId == this.savedUserId && Constants.refreshProfileConstant != 0L){
            Log.d("PROFILE", "Should refresh!");
            Constants.refreshProfileConstant = 0L
            getProfileData()
        }
    }
}