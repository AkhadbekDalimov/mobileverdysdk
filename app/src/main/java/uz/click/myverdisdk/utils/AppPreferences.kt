package uz.click.myverdisdk.utils

import android.content.Context
import android.content.SharedPreferences
import uz.click.myverdisdk.BuildConfig

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