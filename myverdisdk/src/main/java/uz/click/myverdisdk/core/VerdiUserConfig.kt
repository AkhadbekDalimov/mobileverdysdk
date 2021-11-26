package uz.click.myverdisdk.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import uz.click.myverdisdk.core.errors.AppIdEmptyException
import uz.click.myverdisdk.impl.ThemeOptions

/**
 * @author Azamat on 27/09/21
 **/
@Parcelize
data class VerdiUserConfig(
    var appId: String,
    var locale: String,
    var themeMode: ThemeOptions = ThemeOptions.LIGHT,

    ) : Parcelable {
    class Builder {
        private var appId: String? = null
        private var locale: String = "ru"
        private var themeMode: ThemeOptions = ThemeOptions.LIGHT

        fun appId(appId: String) = apply { this.appId = appId }
        fun locale(locale: String) = apply { this.locale = locale }
        fun theme(themeMode: ThemeOptions) = apply { this.themeMode = themeMode }

        fun build(): VerdiUserConfig {
            return VerdiUserConfig(
                appId ?: throw AppIdEmptyException(),
                locale,
                themeMode
            )
        }
    }
}