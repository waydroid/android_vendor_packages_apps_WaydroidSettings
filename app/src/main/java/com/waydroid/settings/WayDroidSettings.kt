package com.waydroid.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.waydroid.settings.adapters.ViewPagerAdapter
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

        // Load ViewPager
        binding.viewPager.apply {
            adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
            TabLayoutMediator(binding.tabLayout, this) { tab, position ->
                tab.text = (adapter as ViewPagerAdapter).tabNames[position]
            }.attach()
        }
    }
}
