package uz.click.myverdisdk.util

import android.util.Log
import uz.click.myverdisdk.core.Verdi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class DocumentInputValidation {

    companion object {
        fun isInputValid(documentInputType: DocumentInputType): Boolean {

            when (documentInputType) {
                is DocumentInputType.BIRTHDAY -> {
                    val format = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
                    val isValidDate = try {
                        format.parse(documentInputType.text);
                        true
                    } catch (e: ParseException) {
                        return false
                    }
                    return documentInputType.text.length == 10 && isValidDate
                }
                is DocumentInputType.PASSPORT -> {
                    val pattern: Pattern = Pattern.compile("[A-Z]{2}[0-9]{7}")
                    val matcher: Matcher = pattern.matcher(documentInputType.text)
                    return documentInputType.text.length >= 9 && matcher.matches()
                }
                is DocumentInputType.EXPIRATION -> {
                    val format = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
                    val isFormatValid = try {
                        format.parse(documentInputType.text);
                        true
                    } catch (e: ParseException) {
                        return false
                    }
                    val inputDate = try {
                        format.parse(documentInputType.text)
                    } catch (exception: Exception) {
                        return false
                    }

                    return documentInputType.text.length == 10 && inputDate.after(Calendar.getInstance().time) && isFormatValid
                }
            }
        }

        fun isPassportInfoValid(): Boolean {
            return isInputValid(DocumentInputType.PASSPORT((Verdi.user.serialNumber)))
                    && isInputValid(DocumentInputType.BIRTHDAY((Verdi.user.birthDate)))
                    && isInputValid(DocumentInputType.EXPIRATION((Verdi.user.dateOfExpiry)))
        }
    }
}