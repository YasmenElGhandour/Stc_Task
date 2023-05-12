package com.ibuild.stc_task_preinterview.ui.postNewItem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.ui.postNewItem.repositry.AddItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(var addItemRepository: AddItemRepository) : ViewModel() {
    private var postLiveData = MutableLiveData<PostsResult>()

    fun getPostsWithPagination() {
//        addItemRepository.addNewPost("","").enqueue(object  : Callback<Posts> {
//            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
//                if (response.body()!=null){
//                //    postLiveData.value = response.body()?.posts[0]
//                }
//                else{
//                    return
//                }
//            }
//            override fun onFailure(call: Call<Posts>, t: Throwable) {
//                Log.d("TAG",t.message.toString())
//            }
//        })


    }

    fun observeMovieLiveData() : LiveData<PostsResult> {
        return postLiveData
    }
}
