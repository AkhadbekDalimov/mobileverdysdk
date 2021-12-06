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
    private var isDetectPassport = false
    private var isDetectIDCard = false

    //region ----- Exposed Methods -----
    override fun stop() {
        textRecognizer.close()
    }

    override fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }

    override fun onSuccess(results: Text) {
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
        fullRead = fullRead.uppercase(Locale.ENGLISH)
        val patternLinePassportType = Pattern.compile(REGEX_OLD_PASSPORT_UZB)
        val matcherLineOldPassportType = patternLinePassportType.matcher(fullRead)
        if (matcherLineOldPassportType.find()) {
            if (!isDetectPassport) {
                resultListener.onDetectPassport(true)
                isDetectPassport = true
                isDetectIDCard = false
            }
            val lastName = matcherLineOldPassportType.group(2)
            val firstName = matcherLineOldPassportType.group(4)
            val serial = matcherLineOldPassportType.group(10) as String
            val documentNumber = cleanDate(matcherLineOldPassportType.group(11))
            val dateOfBirthDay = cleanDate(matcherLineOldPassportType.group(14))
            val expirationDate = cleanDate(matcherLineOldPassportType.group(17))
            val personalNumber = cleanDate(matcherLineOldPassportType.group(19))
            val mrzInfo = createDummyMrz(
                serial + documentNumber,
                dateOfBirthDay,
                expirationDate,
                personalNumber,
                lastName,
                firstName
            )
            onSuccessMrz(mrzInfo)

        } else {
            val patternLineOldIDCardTypes = Pattern.compile(REGEX_OLD_ID_CARD_UZB)
            val matcherLineOldIDCardTypeUzb = patternLineOldIDCardTypes.matcher(fullRead)
            if (matcherLineOldIDCardTypeUzb.find()) {
                if (!isDetectIDCard) {
                    resultListener.onDetectIdCard(true)
                    isDetectPassport = false
                    isDetectIDCard = true
                }
            }
        }
    }

    val listMRZ = ArrayList<MRZInfo>()
    var max: Map.Entry<MRZInfo, List<MRZInfo>>? = null
    private fun onSuccessMrz(mrzInfo: MRZInfo) {
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
                    } else {
                        listMRZ.clear()
                    }
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

    private fun createDummyMrz(
        documentNumber: String?,
        dateOfBirthDay: String?,
        expirationDate: String?,
        personalNumber: String?,
        lastName: String?,
        firstName: String?
    ): MRZInfo {
        return MRZInfo(
            "P",
            "UZB",
            lastName,
            firstName,
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
        fun onDetectPassport(results: Boolean)
        fun onDetectIdCard(isDrCode: Boolean)
    }

    companion object {
        private val TAG = TextRecognitionProcessor::class.java.name
        private const val REGEX_OLD_ID_CARD_UZB: String =
            "(?<documentType>[A-Z<]{2})(?<nationality>[A-Z<]{3})(?<documentNumber>[A-Z0-9<]{9})(?<number>[A-Z0-9<])(?<identificationNumber>[0-9ILDSOG]{14})(?<nn>[-<]{2})(?<dateOfBirth>[0-9ILDSOG]{6})(?<checkDigitDateOfBirth>[0-9ILDSOG]{1})(?<sex>[FM<]){1}(?<expirationDate>[0-9ILDSOG]{6})(?<checkDigitExpiration>[0-9ILDSOG]{1})"
        private const val REGEX_OLD_PASSPORT_UZB =
            "(P<UZB)(?<firstName>[A-Z0-9]{2,20})(<<)(?<lastName>[A-Z0-9]{3,20})(<?)(?<lasName>[A-Z0-9]{3,20})(<*)(«*)(-*)(?<serial>[A-Z<]{2})(?<documentNumber>[0-9<]{7})(?<checkDigitDocumentNumber>[0-9ILDSOG])(?<nationality>[A-Z<]{3})(?<dateOfBirth>[0-9ILDSOG]{6})(?<checkDigitDateOfBirth>[0-9ILDSOG])(?<sex>[FM<])(?<expirationDate>[0-9ILDSOG]{6})(?<checkDigitExpiration>[0-9ILDSOG](?<identificationNumber>[0-9ILDSOG]{14})(?<checkIdentificationNumber>[0-9ILDSOG]{2}))"
    }

    override fun onFailure(e: Exception) {
        resultListener.onDetectPassport(false)
        resultListener.onDetectIdCard(false)
        Log.w(TAG, "Text detection failed.$e")
    }
}