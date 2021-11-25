package com.waydroid.settings.fragments

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import com.waydroid.settings.R
import java.lang.reflect.InvocationTargetException

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)

        switch(findPreference<Preference>(getString(R.string.key_switch_multi_windows_mode))
            as SwitchPreferenceCompat, PROPERTY_WD_MULTI_WINDOWS_KEY)
        switch(findPreference<Preference>(getString(R.string.key_switch_invert_colors))
            as SwitchPreferenceCompat, PROPERTY_WD_INVERT_COLORS_KEY)
        switch(findPreference<Preference>(getString(R.string.key_switch_enable_never_sleep))
            as SwitchPreferenceCompat, PROPERTY_WD_NEVER_SLEEP_KEY)

        seekBar(findPreference<Preference>(getString(R.string.key_seekbar_height_padding))
            as SeekBarPreference, PROPERTY_WD_HEIGHT_PADDING_KEY)
        seekBar(findPreference<Preference>(getString(R.string.key_seekbar_width_padding))
            as SeekBarPreference, PROPERTY_WD_WIDTH_PADDING_KEY)
        seekBar(findPreference<Preference>(getString(R.string.key_seekbar_display_width))
            as SeekBarPreference, PROPERTY_WD_DISPLAY_WIDTH_KEY)
        seekBar(findPreference<Preference>(getString(R.string.key_seekbar_wd_width))
            as SeekBarPreference, PROPERTY_WD_WIDTH_KEY)
    }

    private fun switch(switchPreferenceCompat: SwitchPreferenceCompat, property: String) {
        switchPreferenceCompat.apply {
            isChecked = getBooleanSystemProperties(property)
            onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    preference: Preference ->
                        setProperty(property,
                            (preference as SwitchPreferenceCompat)
                                .isChecked.toString())
                        true
                }
        }
    }

    private fun seekBar(seekBarPreference: SeekBarPreference, property: String) {
        seekBarPreference.apply {
            value = getIntSystemProperties(property)
            onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    setProperty(property, newValue.toString())
                    true
                }
        }
    }

    private fun enableWayDroidSystemUI(enable: Boolean) {
        Log.d(TAG, "enable WayDroid SystemUI $enable")
        setSystemProperties(PROPERTY_BD_SYSTEMUI_KEY, enable.toString())
        val context: Context? = activity
        if (context != null) {
            val packageName = context.packageName
            val intent = Intent("com.android.systemui.action.RESTART")
                .setData(Uri.parse("package://$packageName"))
            val cn = ComponentName(
                "com.android.systemui",
                "com.android.systemui.SysuiRestartReceiver"
            )
            intent.component = cn
            context.sendBroadcast(intent)
        }
    }

    private fun setProperty(mode: String, value: String) {
        Log.d(TAG, "$mode $value")
        setSystemProperties(mode, value)
    }

    private fun setSystemProperties(key: String, value: String) {
        try {
            @SuppressLint("PrivateApi") val clazz = Class.forName(SYSTEM_PROPERTIES_CLASS_NAME)
            val setMethod = clazz.getMethod("set", String::class.java, String::class.java)
            setMethod.invoke(null, key, value)
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "Failed to set value $value for $key")
        } catch (e: NoSuchMethodException) {
            Log.d(TAG, "Failed to set value $value for $key")
        } catch (e: IllegalAccessException) {
            Log.d(TAG, "Failed to set value $value for $key")
        } catch (e: InvocationTargetException) {
            Log.d(TAG, "Failed to set value $value for $key")
        }
    }

    private fun getBooleanSystemProperties(key: String): Boolean {
        try {
            @SuppressLint("PrivateApi")
            val clazz = Class.forName(SYSTEM_PROPERTIES_CLASS_NAME)
            val setMethod = clazz.getMethod(
                "getBoolean",
                String::class.java, Boolean::class.javaPrimitiveType
            )
            return setMethod.invoke(null, key, true) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "Failed to get value for $key")
        } catch (e: NoSuchMethodException) {
            Log.d(TAG, "Failed to get value for $key")
        } catch (e: IllegalAccessException) {
            Log.d(TAG, "Failed to get value for $key")
        } catch (e: InvocationTargetException) {
            Log.d(TAG, "Failed to get value for $key")
        }
        return true
    }

    private fun getIntSystemProperties(key: String): Int {
        try {
            @SuppressLint("PrivateApi")
            val clazz = Class.forName(SYSTEM_PROPERTIES_CLASS_NAME)
            val setMethod = clazz.getMethod(
                "getInt",
                String::class.java, Int::class.javaPrimitiveType
            )
            return setMethod.invoke(null, key, 0) as Int
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "Failed to get value for $key")
        } catch (e: NoSuchMethodException) {
            Log.d(TAG, "Failed to get value for $key")
        } catch (e: IllegalAccessException) {
            Log.d(TAG, "Failed to get value for $key")
        } catch (e: InvocationTargetException) {
            Log.d(TAG, "Failed to get value for $key")
        }
        return 0
    }

    companion object {
        private const val TAG = "WDSettingsFragment"
        private const val SYSTEM_PROPERTIES_CLASS_NAME = "android.os.SystemProperties"
        private const val PROPERTY_BD_SYSTEMUI_KEY = "persist.sys.systemuiplugin.enabled"
        private const val PROPERTY_WD_MULTI_WINDOWS_KEY = "persist.waydroid.multi_windows"
        private const val PROPERTY_WD_INVERT_COLORS_KEY = "persist.waydroid.invert_colors"
        private const val PROPERTY_WD_HEIGHT_PADDING_KEY = "persist.waydroid.height_padding"
        private const val PROPERTY_WD_WIDTH_PADDING_KEY = "persist.waydroid.width_padding"
        private const val PROPERTY_WD_DISPLAY_WIDTH_KEY = "waydroid.display_width"
        private const val PROPERTY_WD_WIDTH_KEY = "persist.waydroid.width"
        private const val PROPERTY_WD_NEVER_SLEEP_KEY = "persist.waydroid.suspend"
    }
}
