package com.ibuild.stc_task_preinterview.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
open class MyApplication : Application() {

    companion object {
        var instance: MyApplication? = null
        var isConnected: Boolean = false // Initial Value
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this);

        if (instance == null) {
            instance = this
        }

    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }


}