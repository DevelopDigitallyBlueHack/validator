package com.cooperthecoder.validator

import android.app.Application
import android.os.StrictMode

class ReaderApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .penaltyDeathOnNetwork()
                    .build())
        }

    }
}