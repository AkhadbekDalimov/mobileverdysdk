package uz.click.myverdisdk.util

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private fun stringToDate(dateStr: String?, dateFormat: DateFormat): Date? {
        var date: Date? = null
        try {
            date = dateFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    private fun dateToString(date: Date?, dateFormat: DateFormat): String {
        return dateFormat.format(date)
    }

    fun convertFromMrzDate(mrzDate: String?): String {
        if (mrzDate.isNullOrEmpty()) return ""
        val date = stringToDate(mrzDate, SimpleDateFormat("yyMMdd"))
        return dateToString(date, SimpleDateFormat("dd.MM.yyyy"))
    }
     fun changeFormatYYDDMM(date: String): String {
        return date.substring(8, 10) + date.substring(3, 5)+date.substring(0,2)
    }

}