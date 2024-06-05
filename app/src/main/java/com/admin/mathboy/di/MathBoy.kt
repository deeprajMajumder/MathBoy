package com.admin.mathboy.di

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MathBoy : Application() {
    private val TAG = MathBoy::class.java.name
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate for MathBoy called")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG,"onTerminate for MathBoy called")
    }
}