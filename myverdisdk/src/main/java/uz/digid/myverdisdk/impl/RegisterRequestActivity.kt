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
                    Verdi.registerListener?.onRegisterError(e)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            finish()
                        }
                    }
                }

                override fun onSuccess(response: RegistrationResponse) {
                    Verdi.result = response.response ?: PersonResult()
                    Verdi.user.scannerSerial = response.response?.clientData?.device?.serialNumber ?: ""
                    Verdi.registerListener?.onRegisterSuccess()
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
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
}