package uz.digid.myverdisdk.impl

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.digid.myverdisdk.R
import uz.digid.myverdisdk.core.Verdi
import uz.digid.myverdisdk.core.callbacks.ResponseListener
import uz.digid.myverdisdk.impl.nfc.ImageUtil
import uz.digid.myverdisdk.model.info.PersonResult
import uz.digid.myverdisdk.model.request.RegistrationResponse

class RegisterRequestActivity : AppCompatActivity() {

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, RegisterRequestActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_request)
        if (Verdi.isUserRegistered) {
            Verdi.verifyPerson(object : ResponseListener<RegistrationResponse> {
                override fun onFailure(e: Exception) {
                    Verdi.verifyListener?.onError(e)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            finish()
                        }
                    }
                }

                override fun onSuccess(response: RegistrationResponse) {
                    Verdi.result = response.response ?: PersonResult()
                    mapPersonResultToFinalResult(Verdi.result)
                    Verdi.verifyListener?.onSuccess()
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            finish()
                        }
                    }
                }
            })
        } else {
            Verdi.registerPerson(object : ResponseListener<RegistrationResponse> {
                override fun onFailure(e: Exception) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            Verdi.registerListener?.onRegisterError(e)
                            finish()
                        }
                    }
                }

                override fun onSuccess(response: RegistrationResponse) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            Verdi.result = response.response ?: PersonResult()
                            mapPersonResultToFinalResult(Verdi.result)
                            Verdi.user.scannerSerial =
                                response.response?.clientData?.device?.serialNumber ?: ""
                            Verdi.registerListener?.onRegisterSuccess(Verdi.user.scannerSerial)
                            finish()
                        }
                    }
                }
            })
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            Verdi.cancelAllRequests()
            finish()
        }
    }

    fun mapPersonResultToFinalResult(personResult: PersonResult) {
        val finalResult = Verdi.finalResult

        finalResult.livenessScore =
            Verdi.result.livenessAnswer?.validateResponse?.livenessScore?.liveness ?: -1.0
        finalResult.similarityScore =
            Verdi.result.livenessAnswer?.validateResponse?.similarityScore?.similarity ?: -1.0
        val bitmapPassport = ImageUtil.convert(Verdi.result.modelPersonPhoto?.personPhoto)
        if (bitmapPassport != null) {
            finalResult.passportPhoto = bitmapPassport
        }
        val bitmapSelfie = ImageUtil.convert(Verdi.result.modelPersonPhoto?.additional)
        if (bitmapSelfie != null) {
            finalResult.selfiePhoto = bitmapSelfie
        }
        finalResult.personPairList = personResult.person?.getPersonPairList()
        finalResult.addressPairList = personResult.address?.getAddressPairList()
    }
}