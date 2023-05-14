package com.ibuild.stc_task_preinterview.ui.list.viewModel

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.Posts
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.ui.list.repository.ListRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ListTest {

    private val testDispatcher = TestCoroutineDispatcher()
    lateinit var mainViewModel: ListViewModel
    lateinit var mainRepository: ListRepo

    @Mock
    lateinit var apiService: ApiServices

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()
    var posts = ArrayList<PostsResult>()
    var emptyPosts = ArrayList<PostsResult>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        mainRepository = ListRepo(apiService)
        mainViewModel = ListViewModel(apiService)
        setList()
    }

    fun setList() {
        posts.add(PostsResult("1","/image1","Movie1","description","20/10/2020","20/10/2020",1))
        posts.add(PostsResult("1","/image1","Movie1","description","20/10/2020","20/10/2020",1))
    }

    @Test
    fun  `get all posts test`() {
        runBlocking {
            Mockito.`when`(mainRepository.getPosts(1)).thenReturn(Response.success(Posts(23,posts)))
            mainViewModel.getAllPostsRequest()
            val result = mainViewModel.postsList().getOrAwaitValue()
            assertEquals(Posts(23,posts), result)
        }
    }

    @Test
    fun `empty posts list test`() {
        runBlocking {
            Mockito.`when`(mainRepository.getPosts(1)).thenReturn(Response.success(Posts(0,emptyPosts)))
            mainViewModel.getAllPostsRequest()
            val result = mainViewModel.postsList().getOrAwaitValue()
            assertEquals(Posts(0,emptyPosts), result)
        }
    }


    //observe live data...
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }

        }
        this.observeForever(observer)
        try {
            afterObserve.invoke()
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set.")
            }
        } finally {
            this.removeObserver(observer)
        }
        @Suppress("UNCHECKED_CAST")
        return data as T
    }

}