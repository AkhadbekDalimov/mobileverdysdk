package uz.digid.myverdisample

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import uz.digid.myverdisdk.core.Verdi
import uz.digid.myverdisdk.core.VerdiUserConfig
import uz.digid.myverdisample.utils.AppPreferences


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(applicationContext)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//        Verdi.logs = true

        val config = VerdiUserConfig
            .Builder()
            .locale("uz")
            .appId("P8g13lFKmXo8TlFO")
            .build()
        Verdi.init(this, config)
    }
}