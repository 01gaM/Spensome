package com.spensome

import android.app.Application
import com.spensome.data.AppContainer
import com.spensome.data.AppDataContainer

class SpensomeApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
