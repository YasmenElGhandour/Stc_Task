package com.ibuild.stc_task_preinterview.ui.postNewItem.repositry

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.InputStream


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddTest {

    @Mock
    lateinit var apiService: ApiServices
    lateinit var addItemRepository: AddItemRepository
    lateinit var addItemViewModel: AddItemViewModel
    var postsResult = PostsResult(null, null, "title", "description", null, null, null)
    val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)
    lateinit var testContext: Context
    private fun getActivity(): Activity? {
        var activity: Activity? = null
        activityRule.scenario.onActivity {
            activity = it
        }
        return activity
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        testContext = InstrumentationRegistry.getInstrumentation().getContext()
        addItemRepository = AddItemRepository(apiService)
        addItemViewModel = AddItemViewModel(addItemRepository)
    }

    @Test
    fun emptyTitleReturnFalse() {
        val result = addItemViewModel.isValidTitle(context, "", true)
        assertThat(result).isFalse()
    }

    @Test
    fun titleReturnTrue() {
        val result = addItemViewModel.isValidTitle(context, "Post Title", true)
        assertThat(result).isTrue()
    }

    fun getBitmapFromTestAssets(fileName: String): Bitmap {
        val assetManager = context.assets
        val testInput: InputStream = assetManager.open(fileName!!)
        return BitmapFactory.decodeStream(testInput)
    }

    @Test
    fun addNewPostReturnNewPost() {
        runBlocking {
            var image = getActivity()?.let {
                addItemViewModel.uploadFile(
                    addItemViewModel.getImageUriFromBitmap(
                        testContext,
                        getBitmapFromTestAssets("post_image.jpeg")
                    ), it
                )
            }
            Mockito.`when`(
                apiService.addNewPost(
                    title = "title",
                    details = "description",
                    image = image
                )
            ).thenReturn(
                Response.success(postsResult)
            )
            val response = addItemRepository.addNewPost(
                title = "title",
                details = "description",
                image = image
            )
            assertEquals(postsResult, response.body())
        }

    }

    @Test
    fun addNewPostReturnTrueIfTitleEmpty() {
        runBlocking {
            var image = getActivity()?.let {
                addItemViewModel.uploadFile(
                    addItemViewModel.getImageUriFromBitmap(
                        testContext,
                        getBitmapFromTestAssets("post_image.jpeg")
                    ), it
                )
            }
            Mockito.`when`(
                apiService.addNewPost(
                    title = "",
                    details = "description",
                    image = image
                )
            ).thenReturn(Response.success(postsResult))
            val response =
                addItemRepository.addNewPost(title = "", details = "description", image = image)
            assertEquals(postsResult, response.body())

        }
    }

    @Test
    fun addNewPostReturnTrueIfDetailsEmpty() {
        runBlocking {
            var image = getActivity()?.let {
                addItemViewModel.uploadFile(
                    addItemViewModel.getImageUriFromBitmap(
                        testContext,
                        getBitmapFromTestAssets("post_image.jpeg")
                    ), it
                )
            }
            Mockito.`when`(
                apiService.addNewPost(
                    title = "test title",
                    details = "",
                    image = image
                )
            ).thenReturn(Response.success(postsResult))
            val response =
                addItemRepository.addNewPost(title = "test title", details = "", image = image)
            assertEquals(postsResult, response.body())

        }
    }
}