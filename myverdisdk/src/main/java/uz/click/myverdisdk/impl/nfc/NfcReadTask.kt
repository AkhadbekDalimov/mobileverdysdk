package uz.click.myverdisdk.impl.nfc

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

class NfcReadTask(private val mCallBack: NfcCallBack) {
    private var person = PersonDetails()
    var docType = DocType.ID_CARD

    fun readNfc(
        context: Context,
        intent: Intent,
        passportNumber: String,
        birthDate: String,
        expirationDate: String
    ) {
        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            val tag = intent.extras?.getParcelable<Tag>(NfcAdapter.EXTRA_TAG) ?: return

            if (listOf(*tag.techList).contains("android.nfc.tech.IsoDep")) {
                if (passportNumber.length == 9 && expirationDate.length == 6 && birthDate.length == 6) {
                    val bacKey: BACKeySpec = BACKey(passportNumber, birthDate, expirationDate)
                    val isoDep = IsoDep.get(tag)
                    addInBackground(context, isoDep, bacKey)
                }
            }
        }
    }

    private fun addInBackground(context: Context, isoDep: IsoDep, bacKey: BACKeySpec) {
        Observable.create<Float> { emitter ->
            try {
                emitter.onNext(0.33f)
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
                        service.getInputStream(PassportService.EF_COM).read()
                    } catch (e: Exception) {
                        service.doBAC(bacKey)
                    }
                }

                // -- Personal Details -- //
                val dg1In = service.getInputStream(PassportService.EF_DG1)
                val dg1File = DG1File(dg1In)
                val mrzInfo = dg1File.mrzInfo
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
                emitter.onNext(0.66f)
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

                if (!emitter.isDisposed) {
                    emitter.onNext(1f)
                }
                emitter.onComplete()
            } catch (e: Throwable) {
                emitter.onError(e)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Float?> {

                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: Float) {
                    mCallBack?.onNext(t)
                }

                override fun onError(e: Throwable) {
                    mCallBack?.onError(e)
                }

                override fun onComplete() {
                    mCallBack?.onComplete(person)
                }
            })
    }

    interface NfcCallBack {
        fun onNext(position: Float)
        fun onError(e: Throwable)
        fun onComplete(person: PersonDetails)
    }
}