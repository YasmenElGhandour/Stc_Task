package com.ibuild.stc_task_preinterview.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostsResult(
    val _id: String,
    val image: String,
    val title: String,
    val details:String,
    val createdAt:String,
    val updatedAt:String,
    val __v:Int,
    ) :Parcelable
