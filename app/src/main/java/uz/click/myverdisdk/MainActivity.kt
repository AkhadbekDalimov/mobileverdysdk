package uz.click.myverdisdk

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import uz.click.myverdisdk.core.VerdiManager
import uz.click.myverdisdk.core.VerdiUser
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.core.callbacks.VerdiScanListener
import uz.click.myverdisdk.core.callbacks.VerdiSelfieListener
import uz.click.myverdisdk.databinding.ActivityMainBinding
import uz.click.myverdisdk.model.request.ModelPersonAnswere
import uz.click.myverdisdk.model.response.AppIdResponse

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanDocument.setOnClickListener {
            VerdiUser.openDocumentScanActivity(this)
            VerdiUser.config.scanListener = object : VerdiScanListener {
                override fun onScanSuccess() {
                    Log.d("ScanTag", VerdiUser.config.toString())
                }
            }
        }

        binding.btnSelfie.setOnClickListener {
            VerdiUser.openSelfieActivity(this)
            VerdiUser.config.selfieListener = object : VerdiSelfieListener {
                override fun onSelfieSuccess(selfie: Bitmap) {
                    binding.ivResult.setImageBitmap(VerdiUser.config.imageFaceBase)
                }
            }
        }

        binding.btnNfc.setOnClickListener {
            VerdiUser.openNfcScanActivity(this)
            VerdiUser.config.scanListener = object : VerdiScanListener {
                override fun onScanSuccess() {
                    Log.d("NfcTag", VerdiUser.config.toString())
                }
            }
        }

        binding.btnCheckAppId.setOnClickListener {
            val verdiManager = VerdiManager()
            verdiManager.checkAppId(this,object : ResponseListener<AppIdResponse> {
                override fun onFailure(e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                }

                override fun onSuccess(response: AppIdResponse) {
                    binding.tvResponse.text = response.message
                }
            })
        }

        binding.btnRegister.setOnClickListener {
            val verdiManager = VerdiManager()
            verdiManager.registerPerson(this,object : ResponseListener<ModelPersonAnswere> {
                override fun onFailure(e: Exception) {
                    Toast.makeText(this@MainActivity, e.cause?.message.toString(), Toast.LENGTH_LONG).show()
                }

                override fun onSuccess(response: ModelPersonAnswere) {
                    binding.tvResponse.text = response.message
                }
            })
        }
    }
}