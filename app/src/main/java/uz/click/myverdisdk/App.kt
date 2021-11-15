package uz.click.myverdisdk

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import uz.click.myverdisdk.core.VerdiUser
import uz.click.myverdisdk.core.VerdiUserConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val config = VerdiUserConfig
            .Builder()
            .appId("P8g13lFKmXo8TlFO")
            .build()
        VerdiUser.init(config, this)
    }
}