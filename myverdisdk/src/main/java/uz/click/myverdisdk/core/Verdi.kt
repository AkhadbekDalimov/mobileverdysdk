package uz.click.myverdisdk.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.os.Handler
import android.provider.Settings
import androidx.annotation.Keep
import uz.click.myverdisdk.core.callbacks.*
import uz.click.myverdisdk.core.errors.NfcInvalidDataException
import uz.click.myverdisdk.core.errors.VerdiNotInitializedException
import uz.click.myverdisdk.impl.nfc.NfcActivity
import uz.click.myverdisdk.impl.scan.ScanActivity
import uz.click.myverdisdk.impl.selfie.SelfieActivity
import uz.click.myverdisdk.model.request.RegistrationResponse
import uz.click.myverdisdk.util.DateUtil

/**
 * @author Azamat on 27/09/21
 * */
@SuppressLint("HardwareIds")
object Verdi {

    lateinit var config: VerdiUserConfig

    lateinit var verdiManager: VerdiManager

    var isAppIdAvailable: Boolean = false

    var verdiListener: VerdiListener? = null

    var verdiNfcListener : VerdiNfcListener? = null

    var verdiUser: VerdiUser = VerdiUser()

    private var nfcAdapter : NfcAdapter? = null

    @[JvmStatic Keep]
    fun init(
        applicationContext: Context,
        config: VerdiUserConfig
    ) {
        this.config = config
        val applicationHandler = Handler(applicationContext.mainLooper)
        verdiManager = VerdiManager(applicationHandler)
        verdiUser.deviceId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        nfcAdapter = NfcAdapter.getDefaultAdapter(applicationContext)
    }

    @[JvmStatic Keep]
    fun openDocumentScanActivity(
        activity: Activity,
        verdiListener: VerdiListener,
        isQrCodeScan: Boolean = false
    ) {
        this.verdiListener = verdiListener
        activity.startActivity(ScanActivity.getInstance(activity, isQrCodeScan))

    }

    @[JvmStatic Keep]
    fun openSelfieActivity(activity: Activity, listener : VerdiListener) {
        this.verdiListener = listener
        activity.startActivity(SelfieActivity.getInstance(activity))
    }

    @[JvmStatic Keep]
    fun openNfcScanActivity(activity: Activity, listener : VerdiNfcListener) {
        if (verdiUser.serialNumber.isEmpty()
            || verdiUser.birthDate.isEmpty()
            || verdiUser.dateOfExpiry.isEmpty()
        ) {
            listener.onNfcError(NfcInvalidDataException())
            return
        }
        this.verdiNfcListener = listener
        activity.startActivity(
            NfcActivity.getInstance(
                activity, verdiUser.serialNumber,
                DateUtil.changeFormatYYDDMM(verdiUser.birthDate),
                DateUtil.changeFormatYYDDMM(verdiUser.dateOfExpiry)
            )
        )
    }

    @[JvmStatic Keep]
    fun registerPerson(listener: ResponseListener<RegistrationResponse>) {
        if (this::verdiManager.isInitialized) {
            verdiManager.registerPerson(listener)
        } else {
            listener.onFailure(VerdiNotInitializedException())
        }
    }

    @[JvmStatic Keep]
    fun verifyPerson(listener: ResponseListener<RegistrationResponse>) {
        if (this::verdiManager.isInitialized) {
            verdiManager.verifyPerson(listener)
        } else {
            listener.onFailure(VerdiNotInitializedException())
        }
    }

    /**
     * @param
     */
    @[JvmStatic Keep]
    fun checkNFCAvailability(listener: VerdiNfcListener) {
        when {
            nfcAdapter == null -> listener.onNfcChecked(isNfcAvailable = false, isNfcEnabled = false)
            nfcAdapter?.isEnabled != true -> listener.onNfcChecked(isNfcAvailable = false, isNfcEnabled = true)
            else -> listener.onNfcChecked(isNfcAvailable = true, isNfcEnabled = true)
        }
    }


}