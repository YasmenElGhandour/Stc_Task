package com.ibuild.stc_task_preinterview.ui.postNewItem.repositry

import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class AddItemRepositoryTest{
    @Inject
    lateinit var apiServices: ApiServices

    var postsResult = PostsResult(null , null , "title","description",null,null,null)
    var postLiveData = MutableLiveData<PostsResult>()

    @Before
    fun setup(){
        postLiveData.value = postsResult
    }

    @Test
    suspend fun nullImageReturnFalse() {
        val result = apiServices.addNewPost(title = "title", details = "description", image = null)
        Truth.assertThat(result).isEqualTo(postLiveData)

    }
}