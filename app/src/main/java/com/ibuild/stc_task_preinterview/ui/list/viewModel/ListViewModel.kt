package com.ibuild.stc_task_preinterview.ui.list.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.ui.list.adapter.PostsAdapter
import com.ibuild.stc_task_preinterview.ui.list.repository.ListDataSource
import com.ibuild.stc_task_preinterview.ui.list.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@HiltViewModel
class ListViewModel @Inject constructor(val  apiServices: ApiServices) : ViewModel() {
    val list = Pager(PagingConfig(pageSize = 10)) {ListDataSource(apiServices)}.liveData.cachedIn(viewModelScope)
}

