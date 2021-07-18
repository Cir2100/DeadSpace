package com.example.deadspace

import android.app.Application
import android.content.Context

//@HiltAndroidApp
class DeadSpace : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {

        lateinit  var appContext: Context

    }
}