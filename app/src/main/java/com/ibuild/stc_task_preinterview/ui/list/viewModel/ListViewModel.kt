package com.ibuild.stc_task_preinterview.ui.list.viewModel

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.Posts
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.ui.list.repository.ListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
 class ListViewModel @Inject constructor(val  apiServices: ApiServices) : ViewModel() {
    val postsList = MutableLiveData<Posts>()
    val list = Pager(PagingConfig(pageSize = 10)) {ListRepo(apiServices)}.liveData.cachedIn(viewModelScope)

     suspend fun getAllPostsRequest() :Response<Posts>{
       var response = apiServices.getPosts(1)
         postsList.postValue(response.body())
         return response
     }

    fun postsList() : LiveData<Posts> {
        return postsList
    }

}

