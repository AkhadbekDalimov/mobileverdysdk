package uz.click.myverdisdk

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import uz.click.myverdisdk.core.VerdiUser
import uz.click.myverdisdk.core.callbacks.VerdiScanListener
import uz.click.myverdisdk.core.callbacks.VerdiSelfieListener
import uz.click.myverdisdk.databinding.ActivityMainBinding

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
    }

    fun onSelfieSuccess() {
//                    binding.ivResult.setImageBitmap(selfie)
        Log.d("OnSelfieSucces", "Called")
    }
}