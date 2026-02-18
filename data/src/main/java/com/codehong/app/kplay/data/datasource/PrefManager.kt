package com.codehong.app.kplay.data.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrefManager @Inject constructor(
    @ApplicationContext private val context: Context?
) {
    fun getBoolean(name: String?, key: String, def: Boolean): Boolean {
        if (context == null) return def

        val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return pref.getBoolean(key, def)
    }

    fun putBoolean(name: String?, key: String, value: Boolean) {
        context?.getSharedPreferences(name, Context.MODE_PRIVATE)?.edit()
            ?.putBoolean(key, value)
            ?.apply()
    }

    fun getString(name: String?, key: String, def: String): String? {
        if (context == null) return def

        val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return pref.getString(key, def)
    }

    fun putString(name: String?, key: String, value: String?) {
        context?.getSharedPreferences(name, Context.MODE_PRIVATE)?.edit()
            ?.putString(key, value)
            ?.apply()
    }

    fun getLong(name: String, key: String, def: Long): Long {
        if (context == null) return def

        val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return pref.getLong(key, def)
    }

    fun putLong(name: String, key: String, value: Long) {
        context?.getSharedPreferences(name, Context.MODE_PRIVATE)?.edit()
            ?.putLong(key, value)
            ?.apply()
    }

    fun getInt(name: String, key: String, def: Int): Int {
        if (context == null) return def

        val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return pref.getInt(key, def)
    }

    fun putInt(name: String, key: String, value: Int) {
        context?.getSharedPreferences(name, Context.MODE_PRIVATE)?.edit()
            ?.putInt(key, value)
            ?.apply()
    }

    fun getStringSet(
        name: String?,
        key: String,
        def: Set<String>
    ): Set<String> {
        if (context == null) return def

        val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return pref.getStringSet(key, def) ?: emptySet()
    }

    fun putStringSet(
        name: String?,
        key: String,
        value: Set<String>
    ) {
        context?.getSharedPreferences(name, Context.MODE_PRIVATE)?.edit()
            ?.putStringSet(key, value)
            ?.apply()
    }
}