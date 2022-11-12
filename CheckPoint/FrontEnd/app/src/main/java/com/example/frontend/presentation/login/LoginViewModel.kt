package com.example.frontend.presentation.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.frontend.common.Resource
import com.example.frontend.common.navigation.Screen
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.use_case.login_user.LoginUseCase
import com.example.frontend.presentation.login.components.LoginState
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private var application: Application
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state : State<LoginState> = _state
    val context = application.baseContext


    fun login(username:String, password:String, navController: NavController)
    {
        loginUseCase(username, password).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = LoginState(token = result.data ?: null)
                    //sacuvaj token
                    GlobalScope.launch(Dispatchers.IO) {
                        DataStoreManager.saveValue(context, "access_token", result.data!!.access_token);
                        DataStoreManager.saveValue(context, "refresh_token", result.data!!.refresh_token);
                    }
                    navController.navigate(Screen.MainLocationScreen.route){
                        popUpTo(Screen.LoginScreen.route)
                    };
                }
                is Resource.Error -> {
                    _state.value = LoginState(error = result.message ?:
                    "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = LoginState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}