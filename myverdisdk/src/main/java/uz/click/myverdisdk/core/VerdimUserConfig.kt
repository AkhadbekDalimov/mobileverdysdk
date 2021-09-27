package uz.click.myverdisdk.core

import uz.click.myverdisdk.core.errors.LocaleEmptyException
import uz.click.myverdisdk.impl.ThemeOptions
import java.io.Serializable

/**
 * @author Azamat on 27/09/21
 * */
data class VerdimUserConfig(
    var locale: String,
    var themeMode: ThemeOptions = ThemeOptions.LIGHT
) : Serializable {
    class Builder {
        private var locale: String? = null
        private var themeMode: ThemeOptions = ThemeOptions.LIGHT

        fun locale(locale: String) = apply { this.locale = locale }
        fun theme(themeMode: ThemeOptions) = apply { this.themeMode = themeMode }
        fun build(): VerdimUserConfig {
            return VerdimUserConfig(
                locale ?: throw LocaleEmptyException(),
                themeMode
            )
        }
    }
}