package com.example.frontend.domain.use_case.get_profile_data

import com.example.frontend.domain.repository.CheckpointRepository
import javax.inject.Inject

class GetMyFollowersUseCase @Inject constructor(
    private val repository: CheckpointRepository
){
//    operator fun invoke(token:String, userId : Long) : Flow<Resource<List<User>>> = flow{
//        try{
//            emit(Resource.Loading())
//            val user = repository.getUser().toU
//            emit(Resource.Success(user));
//        }
//    }
}