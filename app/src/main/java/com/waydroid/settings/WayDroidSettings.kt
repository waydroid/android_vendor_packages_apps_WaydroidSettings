package com.waydroid.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class WayDroidSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_main)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, WayDroidSettingsFragment())
                .commit()

        // Enable back button for preference activity
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
