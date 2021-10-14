package uz.click.myverdisdk.core

import uz.click.myverdisdk.core.errors.AppIdEmptyException
import uz.click.myverdisdk.impl.ThemeOptions
import uz.click.myverdisdk.model.DocType
import java.io.Serializable

/**
 * @author Azamat on 27/09/21
 **/

data class VerdiUserConfig(
    var appId: String,
    var locale: String,
    var themeMode: ThemeOptions = ThemeOptions.LIGHT,
    var documentNumber: String,
    var serialNumber: String,
    var dateOfExpiry: String,
    var birthDate: String,
    var personalNumber: String,
    var docType: String,
) : Serializable {
    class Builder {
        private var appId: String? = null
        private var locale: String = "ru"
        private var themeMode: ThemeOptions = ThemeOptions.LIGHT
        private var documentNumber: String = ""
        private var serialNumber: String = ""
        private var dateOfExpiry: String = ""
        private var birthDate: String = ""
        private var personalNumber: String = ""
        private var docType: String = DocType.PASSPORT

        fun appId(appId: String) = apply { this.appId = appId }
        fun locale(locale: String) = apply { this.locale = locale }
        fun theme(themeMode: ThemeOptions) = apply { this.themeMode = themeMode }
        fun documentNumber(documentNumber: String) = apply { this.documentNumber = documentNumber }
        fun serialNumber(serialNumber: String) = apply { this.serialNumber = serialNumber }
        fun dateOfExpiry(dateOfExpiry: String) = apply { this.dateOfExpiry = dateOfExpiry }
        fun birthDate(birthDate: String) = apply { this.birthDate = birthDate }
        fun personalNumber(personalNumber: String) = apply { this.personalNumber = personalNumber }
        fun docType(docType: String) = apply { this.docType = docType }
        fun build(): VerdiUserConfig {
            return VerdiUserConfig(
                appId ?: throw AppIdEmptyException(),
                locale,
                themeMode,
                documentNumber,
                serialNumber,
                dateOfExpiry,
                birthDate,
                personalNumber,
                personalNumber,
            )
        }
    }
}