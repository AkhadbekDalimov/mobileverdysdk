package uz.click.myverdisdk.impl.nfc

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.sf.scuba.smartcards.CardService
import org.jmrtd.BACKey
import org.jmrtd.BACKeySpec
import org.jmrtd.PassportService
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.DG2File
import org.jmrtd.lds.iso19794.FaceImageInfo
import uz.click.myverdisdk.model.DocType
import uz.click.myverdisdk.model.Image
import uz.click.myverdisdk.model.PersonDetails
import uz.click.myverdisdk.util.LiveEvent

class NfcViewModel : ViewModel() {

    var serialNumber: String? = null
    var expiryDate: String? = null
    var birthDate: String? = null


    val completeRead: LiveEvent<PersonDetails> = LiveEvent()
    val errorRead: LiveEvent<String> = LiveEvent()
    val nextProgress: MutableLiveData<Float> = MutableLiveData()

    private var person = PersonDetails()
    var docType = DocType.ID_CARD


    fun readData(context: Context, intent: Intent?) {
        if (intent == null) return
        if (serialNumber.isNullOrEmpty() || expiryDate.isNullOrEmpty() || birthDate.isNullOrEmpty()) return

        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            val tag = intent.extras?.getParcelable<Tag>(NfcAdapter.EXTRA_TAG) ?: return

            if (listOf(*tag.techList).contains("android.nfc.tech.IsoDep")) {
                if (serialNumber!!.length == 9 && expiryDate!!.length == 6 && birthDate!!.length == 6) {
                    val bacKey: BACKeySpec = BACKey(serialNumber, birthDate, expiryDate)
                    val isoDep = IsoDep.get(tag)
                    addInBackground(context, isoDep, bacKey)
                }
            }
        }
    }

    private fun addInBackground(context: Context, isoDep: IsoDep, bacKey: BACKeySpec) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    withContext(Dispatchers.Main) {
                        nextProgress.value = 0.33f
                    }
                    val cardService = CardService.getInstance(isoDep)
                    cardService.open()
                    val service = PassportService(
                        cardService,
                        PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                        PassportService.DEFAULT_MAX_BLOCKSIZE,
                        false,
                        true
                    )
                    service.open()
                    val paceSucceeded = false
                    service.sendSelectApplet(paceSucceeded)
                    if (!paceSucceeded) {
                        try {
                            kotlin.runCatching {
                                service.getInputStream(PassportService.EF_COM).read()
                            }.onFailure {
                                service.doBAC(bacKey)
                            }
                        } catch (e: Exception) {
                            service.doBAC(bacKey)
                        }
                    }

                    // -- Personal Details -- //
                    val dg1In = service.getInputStream(PassportService.EF_DG1)
                    val dg1File = DG1File(dg1In)
                    val mrzInfo = dg1File.mrzInfo
                    person = PersonDetails()
                    person.name = (
                            mrzInfo.secondaryIdentifier.replace("<", " ").trim { it <= ' ' })
                    person.surname = (
                            mrzInfo.primaryIdentifier.replace("<", " ").trim { it <= ' ' })
                    person.personalNumber = mrzInfo.personalNumber
                    person.gender = mrzInfo.gender.toString()
                    person.serialNumber = mrzInfo.documentNumber
                    person.nationality = mrzInfo.nationality
                    if ("I" == mrzInfo.documentCode) {
                        docType = DocType.ID_CARD
                    } else if ("P" == mrzInfo.documentCode) {
                        docType = DocType.PASSPORT
                    }
                    withContext(Dispatchers.Main) {
                        nextProgress.value = 0.66f
                    }
                    // -- Face Image -- //
                    val dg2In = service.getInputStream(PassportService.EF_DG2)
                    val dg2File = DG2File(dg2In)
                    val faceInfos = dg2File.faceInfos
                    val allFaceImageInfos: MutableList<FaceImageInfo> = ArrayList()
                    for (faceInfo in faceInfos) {
                        allFaceImageInfos.addAll(faceInfo.faceImageInfos)
                    }
                    if (allFaceImageInfos.isNotEmpty()) {
                        val faceImageInfo = allFaceImageInfos.iterator().next()
                        val image: Image = ImageUtil.getImage(context, faceImageInfo)
                        person.faceImage = image.bitmapImage
                        person.faceImageBase64 = image.base64Image
                    }
                    person.docType = docType

                    withContext(Dispatchers.Main) {
                        nextProgress.value = 1f
                        completeRead.postValue(person)
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        errorRead.call()
                    }
                }
            }
        }
    }

}