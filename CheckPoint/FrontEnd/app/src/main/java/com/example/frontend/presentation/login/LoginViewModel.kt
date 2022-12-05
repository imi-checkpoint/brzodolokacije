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
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.use_case.login_user.GetLoginUserIdUseCase
import com.example.frontend.domain.use_case.login_user.LoginUseCase
import com.example.frontend.domain.use_case.session.SessionUseCase
import com.example.frontend.presentation.NavGraphs
import com.example.frontend.presentation.destinations.LoginScreenDestination
import com.example.frontend.presentation.destinations.MainLocationScreenDestination
import com.example.frontend.presentation.login.components.AuthState
import com.example.frontend.presentation.login.components.LoginState
import com.example.frontend.presentation.post.components.PostState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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
    private val sessionUseCase : SessionUseCase,
    private var application: Application
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state : State<LoginState> = _state
    val context = application.baseContext

    private val _authState = mutableStateOf(AuthState())
    val authState : State<AuthState> = _authState

    var savedState = true;
    var access_token  = "";
    var refresh_token = "";

    init {
        //proveri da li je ulogovan, ako jeste prosledi ga na mainlocation
        GlobalScope.launch(Dispatchers.Main){
            access_token =  DataStoreManager.getStringValue(context, "access_token").trim();
            refresh_token = DataStoreManager.getStringValue(context, "refresh_token").trim();

            if(refresh_token != ""){
                Log.d("Auth user", "Auth user");
                Log.d("SAVED TOKEN", "*${refresh_token.trim()}*");
                authUser();
            }
            else{
                savedState = false;
            }
        }
    }

    private fun authUser(){
        sessionUseCase("Bearer "+refresh_token).onEach { result ->
            when(result){
                is Resource.Success -> {
                    Log.d("AUTH SUCCESS", "Success");
                    GlobalScope.launch(Dispatchers.Main) {
                        DataStoreManager.saveValue(context, "access_token", result.data!!.access_token);
                        DataStoreManager.saveValue(context, "refresh_token", result.data!!.refresh_token);

                        val jwtDecode = DataStoreManager.decodeToken(result.data!!.access_token);

                        val username = JSONObject(jwtDecode).getString("sub")
                        DataStoreManager.saveValue(context, "username", username)

                        saveUserId(result.data!!.access_token);

                        _authState.value = AuthState(isAuthorized = true);
                    }

                }
                is Resource.Error -> {
                    _authState.value = AuthState(error = result.message ?:
                    "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _authState.value = AuthState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun login(username:String, password:String)
    {
        Log.d("LOGIN", "Username *${username}*, Password *${password}*");

        loginUseCase(username, password).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = LoginState(token = result.data ?: null)
                    //sacuvaj token
                    GlobalScope.launch(Dispatchers.Main) {
                        Log.d("STORING", "*${result.data!!.refresh_token}*");
                        DataStoreManager.saveValue(context, "access_token", result.data!!.access_token);
                        DataStoreManager.saveValue(context, "refresh_token", result.data!!.refresh_token);

                        val jwtDecode = DataStoreManager.decodeToken(result.data!!.access_token);

                        val username = JSONObject(jwtDecode).getString("sub")
                        DataStoreManager.saveValue(context, "username", username)

                        saveUserId(result.data!!.access_token);
                    }
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
                        Log.d("User id", "Fetched user id ${userId}")
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