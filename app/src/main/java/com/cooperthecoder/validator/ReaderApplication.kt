package com.cooperthecoder.validator

import android.app.Application
import android.os.StrictMode
import com.github.anrwatchdog.ANRWatchDog


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
            ANRWatchDog().start()
        }

    }
}