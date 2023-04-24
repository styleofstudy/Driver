package com.xueng.driver.app

import android.app.Application
import com.xueng.driver.Driver

class App :Application(){
    override fun onCreate() {
        super.onCreate()
        Driver.initialize(this)
    }
}