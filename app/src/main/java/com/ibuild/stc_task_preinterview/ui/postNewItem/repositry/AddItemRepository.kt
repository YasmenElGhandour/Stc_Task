package com.ibuild.stc_task_preinterview.ui.postNewItem.repositry

import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import okhttp3.MultipartBody
import javax.inject.Inject


class AddItemRepository @Inject constructor(val  apiServices: ApiServices)
{

    suspend fun addNewPost(title: String,details: String, image: MultipartBody.Part) =
        apiServices.addNewPost(title = title, details = details, image =image)

}