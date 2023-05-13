package com.ibuild.stc_task_preinterview.di

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ibuild.stc_task_preinterview.BuildConfig
import com.ibuild.stc_task_preinterview.data.Api.ApiServices
import com.ibuild.stc_task_preinterview.utils.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val cacheSize = (5 * 1024 * 1024).toLong()

    @Provides
    @Singleton
    fun hasNetwork(context: Context): Boolean {
        var context = context as MyApplication
        return context.isNetworkConnected()
    }

    @Provides
    @Singleton
    fun provideCache(context: Context): Cache {
        return Cache(File(context.cacheDir, "someIdentifier"), cacheSize)
    }

    @Provides
    @Singleton
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTimeout() = 30L

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }

        OkHttpClient
            .Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .cache(provideCache(context))
            .addNetworkInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context)) {
                    Log.e("testCashing", hasNetwork(context).toString())
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                } else {
                    Log.e("testCashing", hasNetwork(context).toString())

                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                }
                chain.proceed(request)
            }
            .addInterceptor(requestInterceptor)

            .build()
    } else {
        OkHttpClient
            .Builder()
            .retryOnConnectionFailure(true)
            .cache(provideCache(context))
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context)) {
                    Log.e("testCashing", hasNetwork(context).toString())
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()

                } else {
                    Log.e("testCashing", hasNetwork(context).toString())
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                }
                chain.proceed(request)
            }
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, @ApplicationContext context: Context): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiServices =
        retrofit.create(ApiServices::class.java)

}