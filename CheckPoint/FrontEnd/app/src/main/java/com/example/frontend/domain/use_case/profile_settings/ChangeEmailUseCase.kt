package com.example.frontend.domain.use_case.profile_settings

import com.example.frontend.common.Resource
import com.example.frontend.data.remote.dto.UserDTO
import com.example.frontend.data.remote.dto.toPost
import com.example.frontend.data.remote.dto.toUser
import com.example.frontend.domain.model.Post
import com.example.frontend.domain.model.User
import com.example.frontend.domain.repository.CheckpointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ChangeEmailUseCase @Inject constructor(
    private val repository: CheckpointRepository
) {
    operator fun invoke(token : String, user : UserDTO) : Flow<Resource<User>> = flow {
        try{
            emit(Resource.Loading())
            val user = repository.changeUserInfo(token, user).toUser()
            emit(Resource.Success(user));
        }catch (e : HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e : IOException){
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}