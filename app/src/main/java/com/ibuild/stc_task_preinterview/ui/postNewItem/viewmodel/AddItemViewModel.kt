package com.ibuild.stc_task_preinterview.ui.postNewItem.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibuild.stc_task_preinterview.data.model.Posts
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.ui.postNewItem.repositry.AddItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(var addItemRepository: AddItemRepository) : ViewModel() {
     val postLiveData = MutableLiveData<PostsResult>()

     fun addNewPost(title: String, filePart: MultipartBody.Part) {

        GlobalScope.launch {
            postLiveData.postValue(addItemRepository.addNewPost(title,title,filePart).body())
            try {
            }catch (e:Exception){

                Log.d("addNewPost",e.toString())
            }
        }


    }

    fun observePostsLiveData() : LiveData<PostsResult> {
        return postLiveData
    }
}
