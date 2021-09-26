package com.waydroid.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.waydroid.settings.databinding.SettingsMainBinding

class WayDroidSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<SettingsMainBinding>(this, R.layout.settings_main)

        // Set toolbar properties
        binding.toolbarId.apply {
            setSupportActionBar(this)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }

        // load fragment
        supportFragmentManager
                .beginTransaction()
                .replace(binding.settingsContainer.id, WayDroidSettingsFragment())
                .commit()
    }
}
