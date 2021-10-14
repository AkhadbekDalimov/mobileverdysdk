package uz.click.myverdisdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import uz.click.myverdisdk.core.VerdiManager
import uz.click.myverdisdk.core.VerdiUser
import uz.click.myverdisdk.core.VerdiUserConfig
import uz.click.myverdisdk.core.callbacks.VerdiScanListener
import uz.click.myverdisdk.core.callbacks.VerdiUserListener
import uz.click.myverdisdk.databinding.ActivityMainBinding
import uz.click.myverdisdk.impl.ThemeOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanPassport.setOnClickListener {
            VerdiUser.openPassportScanActivity(this, object : VerdiScanListener {
                override fun onScanSuccess() {
                    Log.d("ScanTag", VerdiUser.config.toString())
                }
            })
        }
        binding.btnScanQr.setOnClickListener {
            VerdiUser.openIdCardQrReaderActivity(this, object : VerdiScanListener {
                override fun onScanSuccess() {
                    Log.d("ScanTag", VerdiUser.config.toString())
                }
            })
        }
    }
}