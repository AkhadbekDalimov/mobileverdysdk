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

    }

    private fun addInBackground(context: Context, isoDep: IsoDep, bacKey: BACKeySpec) {
        Observable.create<Float> { emitter ->

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