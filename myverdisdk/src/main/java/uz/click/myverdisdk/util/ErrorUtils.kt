package uz.click.myverdisdk.util

import android.content.Context
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.errors.*
import java.io.IOException
import java.net.HttpRetryException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*


object ErrorUtils {
    fun isApiError(e: Exception): Boolean {
        return when (e) {
            is HttpRetryException -> {
                false
            }
            is SocketTimeoutException -> false
            is IOException -> false
            is UnknownHostException -> false
            is ServerNotAvailableException -> true
            is ServerErrorException -> true
            is UnknownErrorException -> true
            is NumberOfAttemptsException -> true
            else -> false
        }
    }

    fun getErrorMessage(e: Exception, locale: Locale, context: Context): String {
        return when (e) {

            is ServerErrorException -> {
                LanguageUtils.getLocaleStringResource(locale, R.string.server_error, context)
            }
            is InvalidConfirmationCodeException -> {
                LanguageUtils.getLocaleStringResource(
                    locale,
                    R.string.confirmation_code_error,
                    context
                )
            }
            is NumberOfAttemptsException -> {
                LanguageUtils.getLocaleStringResource(
                    locale,
                    R.string.number_of_attempts_error,
                    context
                )
            }
            is UnknownErrorException -> {
                if (e.msg != null)
                    e.msg!!
                else LanguageUtils.getLocaleStringResource(locale, R.string.unknown_error, context)
            }
            else -> {
                LanguageUtils.getLocaleStringResource(locale, R.string.unknown_error, context)
            }
        }
    }

    fun getException(code: Int?, message: String?): Exception {
        return when (code) {
//            -1 -> {
//                WrongServiceIdException(code, message)
//            }
//            -6 -> {
//                MinimalAmountRangeException(code, message)
//            }
//            7 -> {
//                MaximalAmountRangeException(code, message)
//            }
//            -21 -> {
//                ServiceUnavailableForPaymentException(code, message)
//            }
            -302 -> {
                InvalidConfirmationCodeException(code, message)
            }
//            -400 -> {
//                IncorrectCardNumberException(code, message)
//            }
//            -403 -> {
//                InvalidRequestIdException(code, message)
//            }
            -406 -> {
                NumberOfAttemptsException(code, message)
            }
            -500 -> {
                ServerErrorException(code, message)
            }
            else -> {
                UnknownErrorException(code, message)
            }
        }
    }
}