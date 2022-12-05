package com.example.frontend.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.frontend.common.Resource
import com.example.frontend.common.navigation.Screen
import com.example.frontend.domain.model.RegisterUser
import com.example.frontend.domain.use_case.register_user.RegisterUseCase
import com.example.frontend.presentation.destinations.LoginScreenDestination
import com.example.frontend.presentation.register.components.RegisterState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel(){


    private val _state = mutableStateOf(RegisterState())
    val state : State<RegisterState> = _state


    fun register(mail:String , username:String, password:String, passwordRepeat:String, navigator : DestinationsNavigator)
    {
        if(password !== passwordRepeat){
            _state.value = RegisterState(error = "Passwords dont't match!");
        }
        else{
            var user = RegisterUser(mail, username, password );
            registerUseCase(user).onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = RegisterState(message = result.data ?: "")

                        navigator.navigate(
                            LoginScreenDestination()
                        )
                    }
                    is Resource.Error -> {
                        _state.value = RegisterState(error = result.message ?:
                        "An unexpected error occured")
                    }
                    is Resource.Loading -> {
                        _state.value = RegisterState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}