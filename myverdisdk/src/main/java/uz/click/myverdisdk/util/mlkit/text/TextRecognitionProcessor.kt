package uz.click.myverdisdk.util.mlkit.text

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import net.sf.scuba.data.Gender
import org.jmrtd.lds.icao.MRZInfo
import uz.click.myverdisdk.util.GraphicOverlay
import uz.click.myverdisdk.util.mlkit.VisionProcessorBase
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

open class TextRecognitionProcessor(
    context: Context,
    textRecognizerOptions: TextRecognizerOptionsInterface,
    private val resultListener: ResultListener
    ) : VisionProcessorBase<Text>(context) {
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(textRecognizerOptions)

    //region ----- Exposed Methods -----
    override fun stop() {
        textRecognizer.close()
    }

    override fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }

    override fun onSuccess(results: Text, graphicOverlay: GraphicOverlay) {
        var fullRead = ""
        val blocks = results.textBlocks
        for (i in blocks.indices) {

            var temp = ""
            val lines = blocks[i].lines

            for (j in lines.indices) {
                temp += lines[j].text + "-"
            }
            temp = temp.replace("\r".toRegex(), "").replace("\n".toRegex(), "")
                .replace("\t".toRegex(), "").replace(" ", "")
            fullRead += "$temp-"

        }
        fullRead = fullRead.toUpperCase(Locale.ROOT)
        Log.d(TAG, "Read: $fullRead")
        val patternLineOldPassportType = Pattern.compile(REGEX_OLD_PASSPORT)
        val patternLineOldIDCardType = Pattern.compile(REGEX_OLD_ID_CARD_UZB)

        val matcherLineOldPassportType = patternLineOldPassportType.matcher(fullRead)
        val matcherLineOldIDCardType = patternLineOldIDCardType.matcher(fullRead)

        if (matcherLineOldPassportType.find()) {
            resultListener.onDetect(true)
            if (matcherLineOldPassportType.group(3) == "UZB") {
                val patternLineOldPassportTypeUzb = Pattern.compile(REGEX_OLD_PASSPORT_UZB)
                val matcherLineOldPassportTypeUzb = patternLineOldPassportTypeUzb.matcher(fullRead)
                if (matcherLineOldPassportTypeUzb.find()) {
                    val serial = matcherLineOldPassportTypeUzb.group(1) as String
                    val documentNumber = cleanDate(matcherLineOldPassportTypeUzb.group(2))
                    val dateOfBirthDay = cleanDate(matcherLineOldPassportTypeUzb.group(5))
                    val expirationDate = cleanDate(matcherLineOldPassportTypeUzb.group(8))
                    val personalNumber = cleanDate(matcherLineOldPassportTypeUzb.group(10))
                    val mrzInfo = createDummyMrz(
                        "P",
                        serial + documentNumber,
                        dateOfBirthDay,
                        expirationDate,
                        personalNumber
                    )
                    Log.d(
                        TAG,
                        "ReadMRZ: ${mrzInfo.documentNumber} ${mrzInfo.dateOfBirth} ${mrzInfo.dateOfExpiry}"
                    )
                    onSuccess(mrzInfo)

                }
            } else {
                var documentNumber = matcherLineOldPassportType.group(1) as String
                val dateOfBirthDay = cleanDate(matcherLineOldPassportType.group(4))
                val expirationDate = cleanDate(matcherLineOldPassportType.group(7))
                //As O and 0 and really similar most of the countries just removed them from the passport, so for accuracy I am formatting it
                documentNumber = documentNumber.replace("O".toRegex(), "0")
                val mrzInfo =
                    createDummyMrz("P", documentNumber, dateOfBirthDay, expirationDate, "")
                onSuccess(mrzInfo)

            }

        } else if (matcherLineOldIDCardType.find()) {
            resultListener.onDetect(true)
            if (matcherLineOldIDCardType.group(2) == "UZB") {
                val patternLineOldIDCardTypes = Pattern.compile(REGEX_OLD_ID_CARD_UZB)
                val matcherLineOldIDCardTypeUzb = patternLineOldIDCardTypes.matcher(fullRead)
                if (matcherLineOldIDCardTypeUzb.find()) {
                    var documentNumber = matcherLineOldIDCardTypeUzb.group(3) as String
                    val documentType = matcherLineOldIDCardTypeUzb.group(1) as String
                    val dateOfBirthDay = cleanDate(matcherLineOldIDCardTypeUzb.group(7))
                    val expirationDate = cleanDate(matcherLineOldIDCardTypeUzb.group(10))
                    val personalNumber = cleanDate(matcherLineOldIDCardTypeUzb.group(5))
                    documentNumber = documentNumber.replace("O".toRegex(), "0")
                    val mrzInfo = createDummyMrz(
                        "V",
                        documentNumber,
                        dateOfBirthDay,
                        expirationDate,
                        personalNumber
                    )
                    Log.d(
                        TAG,
                        "ReadMRZ: ${mrzInfo.documentNumber} ${mrzInfo.dateOfBirth} ${mrzInfo.dateOfExpiry}"
                    )
                    onSuccess(mrzInfo)
                }
            }
        }
    }

    private fun cleanDate(date: String?): String? {
        var tempDate = date
        tempDate = tempDate?.replace("I".toRegex(), "1")
        tempDate = tempDate?.replace("L".toRegex(), "1")
        tempDate = tempDate?.replace("D".toRegex(), "0")
        tempDate = tempDate?.replace("O".toRegex(), "0")
        tempDate = tempDate?.replace("S".toRegex(), "5")
        tempDate = tempDate?.replace("G".toRegex(), "6")
        return tempDate
    }

    val listMRZ = ArrayList<MRZInfo>()
    var max: Map.Entry<MRZInfo, List<MRZInfo>>? = null
    private fun onSuccess(mrzInfo: MRZInfo) {
        listMRZ.add(mrzInfo)
        when (listMRZ.size) {
            3 -> {
                max = listMRZ.groupBy { it }.entries.maxByOrNull { it.value.size }
                if (max?.value?.size == 3) {
                    resultListener.onSuccess(max!!.value.getOrNull(0))
                }
            }
            6 -> {
                max = listMRZ.groupBy { it }.entries.maxByOrNull { it.value.size }
                if (max?.value?.size!! >= 4) {
                    resultListener.onSuccess(max!!.value.getOrNull(0))
                }
            }
            10 -> {
                max = listMRZ.groupBy { it }.entries.maxByOrNull { it.value.size }
                if (max?.value?.size!! >= 6) {
                    resultListener.onSuccess(max!!.value.getOrNull(0))
                }
            }
            else -> {
                if (listMRZ.size > 10) {
                    max = listMRZ.groupBy { it }.entries.maxByOrNull { it.value.size }
                    if (max?.value?.size!! >= 10) {
                        resultListener.onSuccess(max!!.value.getOrNull(0))
                    }
                }
            }
        }
    }

    private fun createDummyMrz(
        documentType: String?,
        documentNumber: String?,
        dateOfBirthDay: String?,
        expirationDate: String?,
        personalNumber: String?
    ): MRZInfo {
        return MRZInfo(
            documentType,
            "UZB",
            "DUMMY",
            "DUMMY",
            documentNumber,
            "UZB",
            dateOfBirthDay,
            Gender.MALE,
            expirationDate,
            personalNumber
        )
    }

    interface ResultListener {
        fun onSuccess(mrzInfo: MRZInfo?)
        fun onError(exp: Exception?)
        fun onDetect(results: Boolean)
    }

    companion object {
        private val TAG = TextRecognitionProcessor::class.java.name
        private const val REGEX_OLD_PASSPORT =
            "(?<documentNumber>[A-Z0-9<]{9})(?<checkDigitDocumentNumber>[0-9ILDSOG]{1})(?<nationality>[A-Z<]{3})(?<dateOfBirth>[0-9ILDSOG]{6})(?<checkDigitDateOfBirth>[0-9ILDSOG]{1})(?<sex>[FM<]){1}(?<expirationDate>[0-9ILDSOG]{6})(?<checkDigitExpiration>[0-9ILDSOG]{1})"
        private const val REGEX_OLD_ID_CARD_UZB: String =
            "(?<documentType>[A-Z<]{2})(?<nationality>[A-Z<]{3})(?<documentNumber>[A-Z0-9<]{9})(?<number>[A-Z0-9<])(?<identificationNumber>[0-9ILDSOG]{14})(?<nn>[-<]{2})(?<dateOfBirth>[0-9ILDSOG]{6})(?<checkDigitDateOfBirth>[0-9ILDSOG]{1})(?<sex>[FM<]){1}(?<expirationDate>[0-9ILDSOG]{6})(?<checkDigitExpiration>[0-9ILDSOG]{1})"
        private const val REGEX_OLD_PASSPORT_UZB =
            "(?<serial>[A-Z<]{2})(?<documentNumber>[0-9<]{7})(?<checkDigitDocumentNumber>[0-9ILDSOG])(?<nationality>[A-Z<]{3})(?<dateOfBirth>[0-9ILDSOG]{6})(?<checkDigitDateOfBirth>[0-9ILDSOG])(?<sex>[FM<])(?<expirationDate>[0-9ILDSOG]{6})(?<checkDigitExpiration>[0-9ILDSOG](?<identificationNumber>[0-9ILDSOG]{14})(?<checkIdentificationNumber>[0-9ILDSOG]{2}))"
    }

    override fun onFailure(e: Exception) {
        resultListener.onDetect(false)
        Log.w(TAG, "Text detection failed.$e")
    }


}