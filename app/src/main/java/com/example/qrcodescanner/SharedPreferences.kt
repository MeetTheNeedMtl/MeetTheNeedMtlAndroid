package com.example.qrcodescanner

import android.content.Context

object SharedPreferences {

    private const val PREFERENCES_FILE = "app_settings"

    const val isUserLoggedIn = "userLoggedIn"

    fun readBoolean(context: Context, settingName: String, defaultValue: Boolean): Boolean {
        val sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
        return  sharedPref.getBoolean(settingName, defaultValue)
    }

    fun saveBoolean(context: Context, settingName: String, settingValue: Boolean){
        val sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(settingName, settingValue)
        editor.apply()
    }
}