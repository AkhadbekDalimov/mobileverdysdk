package uz.click.myverdisdk.core

import android.content.Context
import android.content.SharedPreferences
import uz.click.myverdisdk.BuildConfig

object VerdiPreferences {

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(BuildConfig.BASE_XML, Context.MODE_PRIVATE)
    }

    var clientPublicKey: String
        get() = preferences.getString(::clientPublicKey.name, "") ?: ""
        set(value) {
            preferences.edit().putString(::clientPublicKey.name, value).apply()
        }

    var deviceSerialNumber: String
        get() = preferences.getString(::deviceSerialNumber.name, "") ?: ""
        set(value) {
            preferences.edit().putString(::deviceSerialNumber.name, value).apply()
        }
}