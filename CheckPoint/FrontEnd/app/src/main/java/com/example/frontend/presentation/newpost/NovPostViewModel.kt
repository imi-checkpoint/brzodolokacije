package com.example.frontend.presentation.newpost

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.frontend.common.Resource
import com.example.frontend.common.navigation.Screen
import com.example.frontend.domain.DataStoreManager
import com.example.frontend.domain.use_case.add_post.AddPhotoUseCase
import com.example.frontend.domain.use_case.add_post.AddPostUseCase
import com.example.frontend.presentation.post.components.PostState
import com.example.frontend.presentation.posts.components.PostsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class NovPostViewModel@Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val addPhotoUseCase: AddPhotoUseCase,
    application: Application
): ViewModel() {
    private val _state = mutableStateOf(PostState())
    val state : State<PostState> = _state
    val context = application.baseContext

    fun savePost(navController: NavController,description: String, locationId:Long,photos:List<Bitmap>){
        GlobalScope.launch(Dispatchers.IO){
            var access_token =  DataStoreManager.getStringValue(context, "access_token");
            var refresh_token = DataStoreManager.getStringValue(context, "refresh_token");

            addPostUseCase("Bearer "+access_token, description,locationId).map { result ->
                when(result){
                    is Resource.Success -> {
                        var i = 0
                        for(photo:Bitmap in photos){
                            val path = context.getExternalFilesDir(null)!!.absolutePath
                            val tempFile = File(path,"tempFileName.jpg")
                            val fOut = FileOutputStream(tempFile)
                            photo.compress(Bitmap.CompressFormat.JPEG,85,fOut)
                            fOut.flush()
                            fOut.close()
                            val file = MultipartBody.Part.createFormData("photo",tempFile.name,
                                RequestBody.create(MediaType.parse("image/*"),tempFile))
                            addPhoto(navController,result.data!!.toLong(),i,file)
                        }
                    }
                    is Resource.Error -> {
                        _state.value = PostState(error = result.message ?:
                        "An unexpected error occured")
                    }
                    is Resource.Loading -> {
                        _state.value = PostState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    fun addPhoto(navController:NavController,postId:Long,order:Int,photo:MultipartBody.Part){
        GlobalScope.launch(Dispatchers.IO){
            var access_token =  DataStoreManager.getStringValue(context, "access_token");
            var refresh_token = DataStoreManager.getStringValue(context, "refresh_token");
            println("Uslo u funkciju")
            addPhotoUseCase("Bearer "+access_token, postId,order,photo).map { result ->
                when(result){
                    is Resource.Success -> {
                        println(result.message)
                        navController.navigate(Screen.MainLocationScreen.route)
                    }
                    is Resource.Error -> {
                        println(result.message)
                        _state.value = PostState(error = result.message ?:
                        "An unexpected error occured")
                    }
                    is Resource.Loading -> {
                        println(result.message)
                        _state.value = PostState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}