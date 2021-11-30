package uz.click.myverdisdk.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.os.Handler
import android.provider.Settings
import androidx.annotation.Keep
import uz.click.myverdisdk.core.callbacks.*
import uz.click.myverdisdk.core.errors.PassportInfoEmptyException
import uz.click.myverdisdk.core.errors.PassportInfoInvalidException
import uz.click.myverdisdk.core.errors.VerdiNotInitializedException
import uz.click.myverdisdk.impl.nfc.NfcActivity
import uz.click.myverdisdk.impl.scan.ScanActivity
import uz.click.myverdisdk.impl.selfie.SelfieActivity
import uz.click.myverdisdk.model.info.PersonResult
import uz.click.myverdisdk.model.request.RegistrationResponse
import uz.click.myverdisdk.util.DateUtil
import uz.click.myverdisdk.util.DocumentInputValidation

/**
 * @author Azamat on 27/09/21
 * */
@SuppressLint("HardwareIds")
object Verdi {

    lateinit var config: VerdiUserConfig

    lateinit var verdiManager: VerdiManager

    var isAppIdAvailable: Boolean = false

    var stateListener: VerdiListener? = null

    var registerListener: VerdiRegisterListener? = null

    var user: VerdiUser = VerdiUser()

    var result = PersonResult()

    private var nfcAdapter: NfcAdapter? = null

    val isNfcAvailable: Boolean
        get() = nfcAdapter != null

    val isNfcEnabled: Boolean
        get() = nfcAdapter?.isEnabled == true

    @[JvmStatic Keep]
    fun init(
        applicationContext: Context,
        config: VerdiUserConfig
    ) {
        this.config = config
        VerdiPreferences.init(applicationContext)
        val applicationHandler = Handler(applicationContext.mainLooper)
        verdiManager = VerdiManager(applicationHandler)
        user.deviceId = Settings.Secure.getString(
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
        this.stateListener = verdiListener
        activity.startActivity(ScanActivity.getInstance(activity, isQrCodeScan))
    }

    @[JvmStatic Keep]
    fun openSelfieActivity(activity: Activity) {
        activity.startActivity(SelfieActivity.getInstance(activity))
    }

    /**
     * It will return NfcInvalidDataException onError method if Passport info is Empty
     * @param Activity
     * @param VerdiListener
     */
    @[JvmStatic Keep]
    fun proceedNfcAndSelfie(
        activity: Activity,
        passportSerialNumber: String,
        birthDate: String,
        dateOfExpiry: String,
        listener: VerdiRegisterListener
    ) {
        this.registerListener = listener
        if (passportSerialNumber.isEmpty()
            || birthDate.isEmpty()
            || dateOfExpiry.isEmpty()
        ) {
            listener.onRegisterError(PassportInfoEmptyException())
            return
        }
        user.serialNumber = passportSerialNumber
        user.birthDate = birthDate
        user.dateOfExpiry = dateOfExpiry

        if (!DocumentInputValidation.isPassportInfoValid()) {
            listener.onRegisterError(PassportInfoInvalidException())
            return
        }
        if (!isNfcAvailable) {
            openSelfieActivity(activity)
        } else {
            activity.startActivity(
                NfcActivity.getInstance(
                    activity, user.serialNumber,
                    DateUtil.changeFormatYYDDMM(user.birthDate),
                    DateUtil.changeFormatYYDDMM(user.dateOfExpiry)
                )
            )
        }
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

}