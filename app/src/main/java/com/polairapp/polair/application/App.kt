package com.polairapp.polair.application

import android.app.Application
import com.facebook.appevents.AppEventsLogger

class App :Application(){
    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this)
    }
}