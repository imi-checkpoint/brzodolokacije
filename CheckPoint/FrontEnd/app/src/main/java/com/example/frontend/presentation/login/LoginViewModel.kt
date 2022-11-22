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
import com.example.frontend.domain.use_case.login_user.GetLoginUserIdUseCase
import com.example.frontend.domain.use_case.login_user.LoginUseCase
import com.example.frontend.presentation.login.components.LoginState
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getLoginUserIdUseCase: GetLoginUserIdUseCase,
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

                        val jwtDecode = DataStoreManager.decodeToken(result.data!!.access_token);

                        val username = JSONObject(jwtDecode).getString("sub")
                        DataStoreManager.saveValue(context, "username", username)

                        saveUserId(result.data!!.access_token);
                    }

                    navController.navigate(Screen.MainLocationScreen.route){
                        popUpTo(Screen.LoginScreen.route){
                            inclusive = true;
                        }
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

    fun saveUserId(access_token: String){
        getLoginUserIdUseCase("Bearer "+access_token).onEach { result ->
            when(result){
                is Resource.Success -> {
                        val userId = result.data
                    if (userId != null) {
                        DataStoreManager.saveValue(context, "userId", userId.toInt())
                    }
                }
                is Resource.Error -> {
                    DataStoreManager.saveValue(context, "userId", 0)
                }
                is Resource.Loading -> {
                    DataStoreManager.saveValue(context, "userId", 0)
                }
            }
        }.launchIn(viewModelScope)
    }

}