package com.dicoding.naufal.moviecatalogue

import android.app.Application
import com.dicoding.naufal.moviecatalogue.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieCatalogApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()

            androidContext(this@MovieCatalogApplication)
            modules(appModule)
        }
    }
}