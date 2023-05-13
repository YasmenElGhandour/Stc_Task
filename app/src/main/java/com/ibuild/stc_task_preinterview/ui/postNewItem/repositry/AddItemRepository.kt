package com.ibuild.stc_task_preinterview.ui.postNewItem.repositry

import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.utils.Resource
import okhttp3.MultipartBody
import okhttp3.Response
import javax.inject.Inject


class AddItemRepository @Inject constructor(val  apiServices: ApiServices)
{

    suspend fun addNewPost(title: String,details: String, image: MultipartBody.Part) : retrofit2.Response<PostsResult> {
      return  apiServices.addNewPost(title = title, details = details, image =image)
    }


}