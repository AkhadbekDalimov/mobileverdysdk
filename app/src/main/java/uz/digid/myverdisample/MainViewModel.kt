package uz.digid.myverdisample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.digid.myverdisdk.util.DocumentInputType
import uz.digid.myverdisdk.util.DocumentInputValidation

class MainViewModel : ViewModel() {
    private var stepMutableLiveData = MutableLiveData<Int>()
    val nextButtonEnabled: MutableLiveData<Boolean> = MutableLiveData()
    var stepLiveData: LiveData<Int> = stepMutableLiveData
    var passportSeries = ""
    var dateOfBirth = ""
    var dateOfExpiry = ""
    fun changeStep(position: Int) {
        stepMutableLiveData.value = position
    }

    fun checkNextButtonEnable() {
        val isEachFieldValid =
            DocumentInputValidation.isInputValid(DocumentInputType.PASSPORT(passportSeries)) &&
                    DocumentInputValidation.isInputValid(DocumentInputType.BIRTHDAY(dateOfBirth)) &&
                    DocumentInputValidation.isInputValid(DocumentInputType.EXPIRATION(dateOfExpiry))
        nextButtonEnabled.postValue(isEachFieldValid)
    }

}