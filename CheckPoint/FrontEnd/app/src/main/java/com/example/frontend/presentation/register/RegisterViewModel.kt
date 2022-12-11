package com.example.frontend.presentation.register

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.frontend.common.Resource
import com.example.frontend.domain.model.RegisterUser
import com.example.frontend.domain.use_case.register_user.RegisterUseCase
import com.example.frontend.presentation.destinations.LoginScreenDestination
import com.example.frontend.presentation.destinations.MainFeedScreenDestination
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
        if(!username.length.equals("")){
            _state.value = RegisterState(error = "Username cant be empty");
        }
        else if(password.length<7){
            println("kratka sifra")
            _state.value = RegisterState(error = "Password must be at least 7 characters");
        }
        else if(password != passwordRepeat){
            println("sifre nisu iste")
            _state.value = RegisterState(error = "Passwords dont't match!");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            println("email nije dobar")
            _state.value = RegisterState(error = "Email not valid");
        }
        else{
            var user = RegisterUser(mail, username, password );
            registerUseCase(user).onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = RegisterState(message = result.data ?: "")

                        navigator.navigate(
                            MainFeedScreenDestination()
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