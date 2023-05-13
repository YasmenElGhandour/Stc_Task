package com.ibuild.stc_task_preinterview.ui.list.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.Posts
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import retrofit2.Response
import javax.inject.Inject


  class ListRepo @Inject constructor(val apiServices: ApiServices ) : PagingSource<Int, PostsResult>() {

    override fun getRefreshKey(state: PagingState<Int, PostsResult>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostsResult> {
        val page = params.key ?: 1

        return try {

            var response = getPosts(page)
            LoadResult.Page(
                data = response.body()?.posts!!,
                prevKey = if (page == 1) null else page - 1 ,
                nextKey = if (response.body()?.posts?.isEmpty()!!) null else page + 1
            )

        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)

        }
    }

     suspend fun getPosts(page : Int) : Response<Posts> {
         return  apiServices.getPosts(page = page)

     }



 }