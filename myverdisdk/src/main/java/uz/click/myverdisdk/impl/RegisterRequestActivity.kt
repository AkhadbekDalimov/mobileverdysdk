package uz.click.myverdisdk.impl

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.Verdi
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.model.info.PersonResult
import uz.click.myverdisdk.model.request.RegistrationResponse

class RegisterRequestActivity : AppCompatActivity() {

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, RegisterRequestActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_request)

        Verdi.registerPerson(object : ResponseListener<RegistrationResponse> {
            override fun onFailure(e: Exception) {
                Verdi.registerListener?.onRegisterError(e)
                lifecycleScope.launch {
                    delay(200)
                    withContext(Dispatchers.Main) {
                        finish()
                    }
                }
            }

            override fun onSuccess(response: RegistrationResponse) {
                Verdi.result = response.response ?: PersonResult()
                Verdi.registerListener?.onRegisterSuccess()
                lifecycleScope.launch {
                    delay(200)
                    withContext(Dispatchers.Main) {
                        finish()
                    }
                }
            }
        })

        findViewById<Button>(R.id.btnAction).setOnClickListener {
            finish()
        }
    }
}