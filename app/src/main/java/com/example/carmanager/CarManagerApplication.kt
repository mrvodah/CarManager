package com.example.carmanager

import android.app.Application
import com.example.carmanager.util.PrefManager

class CarManagerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        PrefManager.with(this)
    }

}