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
 * The central Verdi class, where all the important configurations and actions happen
 * @author Azamat
 * */
@SuppressLint("HardwareIds")
object Verdi {

    lateinit var config: VerdiUserConfig

    lateinit var verdiManager: VerdiManager

    var isAppIdAvailable: Boolean = false

    var stateListener: VerdiListener? = null

    var registerListener: VerdiRegisterListener? = null

    var verifyListener: VerdiListener? = null

    var user: VerdiUser = VerdiUser()

    var result = PersonResult()

    private var nfcAdapter: NfcAdapter? = null

    val isNfcAvailable: Boolean
        get() = nfcAdapter != null

    val isNfcEnabled: Boolean
        get() = nfcAdapter?.isEnabled == true

    val isUserRegistered: Boolean
        get() = user.scannerSerial.isNotEmpty()

    /**
     * This method is a starter method. It should be called preferably in the Application class,
     * or before using any other methods. VerdiUserConfig should be built and passed, so that other
     * methods of the SDK work expectedly
     * @param applicationContext
     * @param config VerdiUserConfig to initialize basic requirements like AppId
     */
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

    /**
     * This method will scan Passport or Id Card
     * Inside the activity Camera permission is required
     * @param activity It will start ScanActivity
     * @param verdiListener It will be callback of the results
     * @param isQrCodeScan by default It will scan Passport. To change it to scan ID Card Pass true
     */
    @[JvmStatic Keep]
    fun openDocumentScanActivity(
        activity: Activity,
        verdiListener: VerdiListener,
        isQrCodeScan: Boolean = false
    ) {
        this.stateListener = verdiListener
        activity.startActivity(ScanActivity.getInstance(activity, isQrCodeScan))
    }

    /**
     * This method will take a selfie and send the corresponding request
     * @param activity It will start ScanActivity
     * @param verdiListener It will be callback of the results
     * @param isQrCodeScan by default It will scan Passport. To change it to scan ID Card Pass true
     */
    @[JvmStatic Keep]
    fun openSelfieActivity(
        activity: Activity,
        verifyListener: VerdiListener? = null,
        scannerSerialNumber: String = ""
    ) {
        user.scannerSerial = scannerSerialNumber
        this.verifyListener = verifyListener
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

    internal fun cancelAllRequests() {
        verdiManager.cancelAllRunningCalls()
    }

}