package com.example.frontend.domain.use_case.delete_post

import com.example.frontend.common.Resource
import com.example.frontend.domain.repository.CheckpointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository : CheckpointRepository
) {

    operator fun invoke(token : String, postId: Long) : Flow<Resource<String>> = flow {
        try{
            emit(Resource.Loading())
            val message = repository.deletePostById(token, postId)
            emit(Resource.Success(message))
        }catch (e : HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e : IOException){
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }

}
