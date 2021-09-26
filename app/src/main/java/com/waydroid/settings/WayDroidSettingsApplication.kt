package com.waydroid.settings

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class WayDroidSettingsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
    }
}
