package com.ibuild.stc_task_preinterview.data.Api

import com.ibuild.stc_task_preinterview.data.model.Posts
import com.ibuild.stc_task_preinterview.utils.Resource
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiServices {

    @GET("api/posts")
    suspend fun getPosts(@Query("page") page: Int): Response<Posts>


//    @FormUrlEncoded
//    @POST("api/posts")
//    suspend fun addNewPost(@Query("title") title: String ,@Part image: MultipartBody.Part?): Response<Posts>

}