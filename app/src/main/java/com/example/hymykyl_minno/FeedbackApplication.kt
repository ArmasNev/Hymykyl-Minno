package com.example.hymykyl_minno

import android.app.Application
import com.example.hymykyl_minno.data.AppContainer
import com.example.hymykyl_minno.data.AppDataContainer

class FeedbackApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
