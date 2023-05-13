package com.ibuild.stc_task_preinterview.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostsResult(
    val _id: String? = null,
    val image: String?= null,
    var title: String,
    var details:String,
    val createdAt:String?= null,
    val updatedAt:String?= null,
    val __v:Int?= null,
    ) :Parcelable
