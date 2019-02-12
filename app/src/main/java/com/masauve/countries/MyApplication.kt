package com.masauve.countries

import android.app.Application
import com.masauve.countries.di.appModule
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            logger()
            modules(appModule)
        }
    }
}