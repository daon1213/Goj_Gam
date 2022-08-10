package com.daon.goj_gam

import android.app.Application
import android.content.Context
import com.daon.goj_gam.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GojGam : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin{
            androidContext(this@GojGam)
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
        private set
    }

}