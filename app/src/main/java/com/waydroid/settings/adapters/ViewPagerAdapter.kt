package com.waydroid.settings.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waydroid.settings.WayDroidSettingsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    val tabNames = arrayListOf("Settings", "Info", "Docs")

    override fun getItemCount(): Int = tabNames.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> WayDroidSettingsFragment()
            2 -> WayDroidSettingsFragment()
            else -> WayDroidSettingsFragment()
        }
    }
}