package com.dicoding.naufal.moviecatalogue.data.local.pref

import android.content.Context
import android.content.SharedPreferences

class FirstRunPreference(context: Context) {
    private val preferences: SharedPreferences

    var firstRun: Boolean
        get() {
            return preferences.getBoolean(FIRST_RUN, true)
        }
        set(value) {
            val editor = preferences.edit()
            editor.putBoolean(FIRST_RUN, value)
            editor.apply()
        }

    init {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private val PREFS_NAME = "movie_catalog_pref"
        private val FIRST_RUN = "first_run"
    }
}