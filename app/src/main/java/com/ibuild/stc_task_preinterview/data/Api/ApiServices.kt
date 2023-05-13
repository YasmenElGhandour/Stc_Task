package com.ibuild.stc_task_preinterview.data.Api

import com.ibuild.stc_task_preinterview.data.model.Posts
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiServices {

    @GET("api/posts")
    suspend fun getPosts(@Query("page") page: Int): Response<Posts>
    @Multipart
    @POST("api/posts")
    suspend fun addNewPost(@Query("title") title: String, @Query("details") details: String, @Part image: MultipartBody.Part?): Response<PostsResult>

}