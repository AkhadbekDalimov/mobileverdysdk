package uz.click.myverdisdk.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import uz.click.myverdisdk.core.errors.AppIdEmptyException
import uz.click.myverdisdk.impl.ThemeOptions

/**
 * Verdi Configuration
 * App Id initialization is the starting point of the SDK.
 * Thus, Here AppId must be passed in order to proceed to the next actions
 * AppId can be given by the DIGID company
 * @author Azamat
 **/
@Parcelize
data class VerdiUserConfig(
    var appId: String,
    var locale: String,
) : Parcelable {
    class Builder {
        private var appId: String? = null
        private var locale: String = "ru"

        fun appId(appId: String) = apply { this.appId = appId }
        fun locale(locale: String) = apply { this.locale = locale }

        fun build(): VerdiUserConfig {
            return VerdiUserConfig(
                appId ?: throw AppIdEmptyException(),
                locale,
            )
        }
    }
}