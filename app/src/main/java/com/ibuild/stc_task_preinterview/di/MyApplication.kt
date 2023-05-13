package com.ibuild.stc_task_preinterview.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Cache


@HiltAndroidApp
class MyApplication  : Application(){

    private var instance: MyApplication? = null
    var isConnected: Boolean = false // Initial Value


    override fun onCreate() {
        super.onCreate()
        if (instance == null) {
            instance = this
        }
    }

    fun getInstance(): MyApplication? {
        return instance
    }

     fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
         isConnected = true
        return isConnected
    }

}