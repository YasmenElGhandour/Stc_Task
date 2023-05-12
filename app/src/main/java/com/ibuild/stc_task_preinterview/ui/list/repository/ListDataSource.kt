package com.ibuild.stc_task_preinterview.ui.list.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import javax.inject.Inject


 class ListDataSource @Inject constructor(val apiServices: ApiServices) : PagingSource<Int, PostsResult>() {

    override fun getRefreshKey(state: PagingState<Int, PostsResult>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostsResult> {
        val page = params.key ?: 1

        return try {

            val data = apiServices.getPosts(page)
            Log.d("TAG", "load: ${data.body()}")
            LoadResult.Page(
                data = data.body()?.posts!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.body()?.posts?.isEmpty()!!) null else page + 1
            )


        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }


    }


}