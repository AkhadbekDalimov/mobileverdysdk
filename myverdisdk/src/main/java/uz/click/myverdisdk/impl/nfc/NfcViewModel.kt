package uz.click.myverdisdk.impl.nfc

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.click.myverdisdk.model.PersonDetails
import uz.click.myverdisdk.util.LiveEvent

class NfcViewModel : ViewModel() {

    var serialNumber: String? = null
    var expiryDate: String? = null
    var birthDate: String? = null


    val completeRead: LiveEvent<PersonDetails> = LiveEvent()
    val errorRead: LiveEvent<String> = LiveEvent()
    val nextProgress: MutableLiveData<Float> = MutableLiveData()

    val callback = object : NfcReadTask.NfcCallBack {
        override fun onComplete(person: PersonDetails) {
            completeRead.postValue(person)
        }

        override fun onError(e: Throwable) {
            errorRead.call()
        }

        override fun onNext(position: Float) {
            nextProgress.postValue(position)
        }
    }

    private val nfcReadTask by lazy { NfcReadTask(callback) }


    fun readData(context: Context, intent: Intent?) {
        if (intent == null) return
        if (serialNumber.isNullOrEmpty() || expiryDate.isNullOrEmpty() || birthDate.isNullOrEmpty()) return

        nfcReadTask.readNfc(
            context,
            intent,
            serialNumber!!,
            birthDate!!,
            expiryDate!!
        )
    }

}