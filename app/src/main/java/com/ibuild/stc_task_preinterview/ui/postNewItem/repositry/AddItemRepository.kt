package com.ibuild.stc_task_preinterview.ui.postNewItem.repositry

import android.net.Uri
import androidx.core.net.toUri
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import okhttp3.MultipartBody
import java.io.File
import java.io.FileInputStream
import java.net.URI
import javax.inject.Inject


class AddItemRepository @Inject constructor(val  apiServices: ApiServices)
{
    suspend fun addNewPost(title: String?,details: String?, image: MultipartBody.Part?) : retrofit2.Response<PostsResult> {
      return  apiServices.addNewPost(title = title, details = details, image =image)
    }


}