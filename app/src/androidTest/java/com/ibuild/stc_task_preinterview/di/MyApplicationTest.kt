package com.ibuild.stc_task_preinterview.di

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MyApplicationTest {

    lateinit var myApplication: MyApplication
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        myApplication = MyApplication()
    }


    @After
    fun tearDown() {
    }

    @Test
    fun isNetworkConnected() {
        assertTrue(myApplication.isNetworkConnected(context))
    }
}