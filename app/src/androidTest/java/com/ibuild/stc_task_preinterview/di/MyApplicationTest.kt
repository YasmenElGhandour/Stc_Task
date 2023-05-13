package com.ibuild.stc_task_preinterview.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@RunWith(JUnit4::class)
class MyApplicationTest {

    lateinit var myApplication: MyApplication
    @Before
    fun setUp() {
        myApplication = MyApplication()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun isNetworkConnected() {
        myApplication.isNetworkConnected()
        assertTrue(myApplication.isNetworkConnected())
    }
}