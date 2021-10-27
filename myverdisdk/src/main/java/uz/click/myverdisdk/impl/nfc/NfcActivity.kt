package uz.click.myverdisdk.impl.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import uz.click.myverdisdk.R

class NfcActivity : AppCompatActivity() {

    private val viewModel: NfcViewModel by viewModels()

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            nfcStateChanged(intent)
        }
    }

    private var animationView: LottieAnimationView? = null
    private var textViewNfcTitle: TextView? = null
    var adapter: NfcAdapter? = null

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, NfcActivity::class.java)
        }
    }


    override fun onStart() {
        super.onStart()
        checkNfcState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        val filter = IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)
        registerReceiver(mReceiver, filter)

        //todo nfc
        viewModel.birthDate = "27.07.1998"
        viewModel.expiryDate = "22.05.2025"
        viewModel.serialNumber = "AA9798910"

        animationView = findViewById(R.id.animation_view)
        textViewNfcTitle = findViewById(R.id.textViewNfcTitle)



        adapter = NfcAdapter.getDefaultAdapter(this)


        showAnimation()


        viewModel.errorRead.observe(this, {
            findViewById<View>(R.id.progressBar)?.visibility = View.GONE
            showDialog()
        })

        viewModel.nextProgress.observe(this, { pos ->
            findViewById<View>(R.id.progressBar)?.visibility = View.VISIBLE
            animationView?.apply {
                if (progress == 0f) {
                    setAnimation(R.raw.step_progress)
                    speed = 3f
                    setMaxProgress(pos)
                    playAnimation()
                } else {
                    setMaxProgress(pos)
                    resumeAnimation()
                }
            }

        })

        viewModel.completeRead.observe(this, {
            findViewById<View>(R.id.progressBar)?.visibility = View.GONE
            //todo nfc config set
            val person = it

        })
    }

    private fun showDialog() {
        val dialog = ErrorNfcDialog(this)
        dialog.show()
    }

    private fun showAnimation() {
        if (adapter != null) {
            if (adapter!!.isEnabled) {
                animationView?.playAnimation()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        onDisableNfc()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    private fun nfcStateChanged(intent: Intent) {
        val action = intent.action
        if (action == NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) {
            val state = intent.getIntExtra(
                NfcAdapter.EXTRA_ADAPTER_STATE,
                NfcAdapter.STATE_OFF
            )
            when (state) {
                NfcAdapter.STATE_OFF -> {
                    checkNfcState()
                }
                NfcAdapter.STATE_TURNING_OFF -> {
                    Log.d("NFC", "STATE_TURNING_OFF")
                }
                NfcAdapter.STATE_ON -> {
                    checkNfcState()
                }
                NfcAdapter.STATE_TURNING_ON -> {
                    Log.d("NFC", "STATE_TURNING_ON")
                }
            }
        }
    }

    private fun checkNfcState() {
        if (adapter != null) {
            if (adapter!!.isEnabled) {
                showAnimation()
                textViewNfcTitle?.visibility = View.INVISIBLE
                val intent = Intent(this, this.javaClass)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                val pendingIntent =
                    PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                val filter = arrayOf(arrayOf("android.nfc.tech.IsoDep"))
                if (this.isFinishing.not())
                    adapter!!.enableForegroundDispatch(
                        this,
                        pendingIntent,
                        null,
                        filter
                    )
            } else {
                textViewNfcTitle?.apply {
                    setText(R.string.nfc_is_not_enabled)
                    visibility = View.VISIBLE
                }
            }
        } else {
            textViewNfcTitle?.apply {
                setText(R.string.nfc_is_not_available)
                visibility = View.VISIBLE
            }
        }
    }

    private fun onDisableNfc() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.readData(this, intent)
    }
}