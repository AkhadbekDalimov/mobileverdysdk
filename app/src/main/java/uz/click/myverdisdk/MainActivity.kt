package uz.click.myverdisdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.click.myverdisdk.core.VerdimUser
import uz.click.myverdisdk.core.VerdimUserConfig
import uz.click.myverdisdk.core.callbacks.VerdimUserListener
import uz.click.myverdisdk.databinding.ActivityMainBinding
import uz.click.myverdisdk.impl.ThemeOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerdim.setOnClickListener {
            val config = VerdimUserConfig.Builder()
                .locale("EN")
                .theme(ThemeOptions.LIGHT)
                .build()
            VerdimUser.init(supportFragmentManager, config, object : VerdimUserListener {
                override fun onSuccess() {

                }
            })
        }
    }
}