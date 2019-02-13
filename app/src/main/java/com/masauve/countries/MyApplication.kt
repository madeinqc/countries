package com.masauve.countries

import android.app.Application
import com.masauve.countrylist.di.countryListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            logger()
            modules(countryListModule)
            androidContext(this@MyApplication)
        }
    }
}