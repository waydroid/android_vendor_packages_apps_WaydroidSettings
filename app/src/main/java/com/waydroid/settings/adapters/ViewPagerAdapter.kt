package com.waydroid.settings.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waydroid.settings.fragments.AboutFragment
import com.waydroid.settings.fragments.InfoFragment
import com.waydroid.settings.fragments.SettingsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val tabNames = arrayListOf("Settings", "Info", "About")

    override fun getItemCount(): Int = tabNames.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SettingsFragment()
            1 -> InfoFragment.newInstance()
            else -> AboutFragment.newInstance()
        }
    }
}
