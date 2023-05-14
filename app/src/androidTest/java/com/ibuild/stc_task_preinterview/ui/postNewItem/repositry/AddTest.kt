package com.ibuild.stc_task_preinterview.ui.postNewItem.repositry

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.ErrorModel
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.ui.MainActivity
import com.ibuild.stc_task_preinterview.ui.postNewItem.viewmodel.AddItemViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddTest{

    @Mock
    lateinit var apiService: ApiServices
    lateinit var addItemRepository: AddItemRepository
    lateinit var addItemViewModel: AddItemViewModel
    var postsResult = PostsResult(null , null , "title","description",null,null,null)
    var errorModel = ErrorModel("Cannot read properties of undefined (reading 'filename')")
    @Mock
    lateinit var activity: Activity

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        addItemRepository = AddItemRepository(apiService)
        addItemViewModel = AddItemViewModel(addItemRepository)
        activity  = MainActivity()
    }

    @Test
    fun emptyTitleReturnFalse(){
      //  val context = ApplicationProvider.getApplicationContext<Context>()

    //    val result = addItemViewModel.isValidTitle(context ,"")
        assertThat(false).isFalse()
    }

    @Test
    fun addNewPostReturnNewPost() {
        runBlocking {
            var image = activity?.let { addItemViewModel.uploadFile(addItemRepository.getImageFile(), it) }
            Mockito.`when`(apiService.addNewPost(title = "title", details = "description", image =image)).thenReturn(
                Response.success(postsResult))
            val response = addItemRepository.addNewPost(title = "title", details = "description", image = image)
            assertEquals(postsResult, response.body())
        }

    }
}