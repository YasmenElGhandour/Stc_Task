package com.ibuild.stc_task_preinterview.ui.list.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import javax.inject.Inject


class ListRepository
@Inject constructor(val apiServices: ApiServices)
{
    suspend fun getPosts(page : Int) =  apiServices.getPosts(page = page)
}