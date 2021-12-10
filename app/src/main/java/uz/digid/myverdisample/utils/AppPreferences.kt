package uz.digid.myverdisample.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences("VerdiSample", Context.MODE_PRIVATE)
    }

    var scannerSerialNumber: String
        get() = preferences.getString(::scannerSerialNumber.name, "") ?: ""
        set(value) {
            preferences.edit().putString(::scannerSerialNumber.name, value).apply()
        }

}