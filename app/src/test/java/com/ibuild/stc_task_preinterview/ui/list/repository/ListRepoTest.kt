package com.ibuild.stc_task_preinterview.ui.list.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.ui.list.viewModel.ListViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection

class ListRepoTest {
    private lateinit var mockWebServer: MockWebServer

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var listRepo: ListRepo

    @Mock
    private lateinit var apiServices: ApiServices

    val posts = ArrayList<PostsResult>()

    @Before
    fun setUp() {
//         val context = ApplicationProvider.getApplicationContext<Context>()
        // System.setProperty("javax.net.ssl.trustStoreType", "JKS")
//
        mockWebServer = MockWebServer()
        mockWebServer.start()

        MockitoAnnotations.initMocks(this)
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        apiServices = mock(ApiServices::class.java)
        listRepo = mock(ListRepo::class.java)

        addList()

    }

    private fun addList() {
        posts.add(PostsResult("1","/image1","Movie1","description","20/10/2020","20/10/2020",1))
        posts.add(PostsResult("1","/image1","Movie1","description","20/10/2020","20/10/2020",1))
    }

//    Mockito.`when`(listRepo.getPosts(1).body()?.posts).thenReturn(posts)

    @Test
    fun `for posts not available, api must return with http code 404 and null posts list`() =
        runTest {
            launch {
                val expectedResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                mockWebServer.enqueue(expectedResponse)
                testDispatcher.scheduler.advanceUntilIdle()
                val actualResponse = listRepo.getPosts(1)
                assertThat(actualResponse.code()).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND)
                assertThat(actualResponse).isNull()
            }

        }

    @Test
    fun `for no posts, api must return empty with http code 200`() = runTest {
        val posts = emptyList<PostsResult>()
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(posts))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = listRepo.getPosts(1)
        assertThat(actualResponse.body()?.posts).hasSize(0)
    }

    @Test
    fun `for multiple posts, api must return all posts with http code 200`() = runTest {
        val posts = listOf(
            PostsResult("1","/image1","Movie1","description","20/10/2020","20/10/2020",1),
            PostsResult("2","/image2","Movie2","description","20/10/2020","20/10/2020",2),
        )
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(posts))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = listRepo.getPosts(1)
        assertThat(actualResponse.body()?.posts).hasSize(2)
        assertThat(actualResponse.body()?.posts).isEqualTo(posts)
    }

    @Test
    fun `for server error, api must return with http code 5xx`() = runTest {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = listRepo.getPosts(1)
        assertThat(actualResponse.code()).isEqualTo(HttpURLConnection.HTTP_INTERNAL_ERROR)
    }

    @Test
    fun `for server error, api must return with http code 5xx and error message`() = runTest {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = listRepo.getPosts(1)
        assertThat(actualResponse.code()).isEqualTo(HttpURLConnection.HTTP_INTERNAL_ERROR)
        assertThat(actualResponse.message()).isEqualTo("server error")
    }



    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}