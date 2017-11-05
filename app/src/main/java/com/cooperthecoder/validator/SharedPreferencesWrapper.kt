package com.cooperthecoder.validator

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesWrapper {
    private const val PREFS_NAME = "prefs"
    private const val PREFS_KEY_BASE_FACE = "base_face"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun edit(context: Context): SharedPreferences.Editor {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
    }

    fun baseFaceId(context: Context): String? {
        val baseFaceId = prefs(context).getString(PREFS_KEY_BASE_FACE, "")
        return if (baseFaceId != "") {
            baseFaceId
        } else {
            null
        }
    }

    fun saveBaseFaceId(context: Context, faceId: String) {
        edit(context)
                .putString(PREFS_KEY_BASE_FACE, faceId)
                .commit()
    }
}