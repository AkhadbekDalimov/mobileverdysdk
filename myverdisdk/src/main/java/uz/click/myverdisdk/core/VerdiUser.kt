package uz.click.myverdisdk.core

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.widget.Toast
import androidx.annotation.Keep
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.core.callbacks.VerdiNfcCheckListener
import uz.click.myverdisdk.core.errors.NFCNotEnabledException
import uz.click.myverdisdk.core.errors.NfcInvalidDataException
import uz.click.myverdisdk.impl.nfc.NfcActivity
import uz.click.myverdisdk.impl.scan.ScanActivity
import uz.click.myverdisdk.impl.selfie.SelfieActivity
import uz.click.myverdisdk.model.request.RegistrationResponse
import uz.click.myverdisdk.model.response.AppIdResponse
import uz.click.myverdisdk.util.DateUtil

/**
 * @author Azamat on 27/09/21
 * */
object VerdiUser {

    lateinit var config: VerdiUserConfig

    //TODO temporary
    lateinit var verdiManager: VerdiManager

    @[JvmStatic Keep]
    fun init(
        config: VerdiUserConfig,
        //TODO temporary
        context: Context
    ) {
        this.config = config
        verdiManager = VerdiManager(context)
    }

    @[JvmStatic Keep]
    fun openDocumentScanActivity(activity: Activity, isQrCodeScan: Boolean = false) {
        activity.startActivity(ScanActivity.getInstance(activity, isQrCodeScan))
    }

    @[JvmStatic Keep]
    fun openSelfieActivity(activity: Activity) {
        activity.startActivity(SelfieActivity.getInstance(activity))
    }

    @[JvmStatic Keep]
    fun openNfcScanActivity(activity: Activity) {
        if (config.serialNumber.isEmpty()
            || config.birthDate.isEmpty()
            || config.dateOfExpiry.isEmpty()
        ) {
            config.nfcListener?.onNfcError(NfcInvalidDataException())
            return
        }
        activity.startActivity(
            NfcActivity.getInstance(
                activity, config.serialNumber,
                DateUtil.changeFormatYYDDMM(config.birthDate),
                DateUtil.changeFormatYYDDMM(config.dateOfExpiry)
            )
        )
    }

    @[JvmStatic Keep]
    fun registerPerson(activity: Activity, listener: ResponseListener<RegistrationResponse>) {
        if (this::verdiManager.isInitialized) {
            verdiManager.registerPerson(activity, listener)
        }
    }

    @[JvmStatic Keep]
    fun checkAppId(activity: Activity, listener: ResponseListener<AppIdResponse>) {
        if (this::verdiManager.isInitialized) {
            verdiManager.checkAppId(activity, listener)
        }
    }

    @[JvmStatic Keep]
    fun checkNFCAvailability(context: Context, listener: VerdiNfcCheckListener) {
        val adapter = NfcAdapter.getDefaultAdapter(context)
        if (adapter == null)
            listener.onNfcChecked(isNfcAvailable = false, isNfcEnabled = false)
        else if (!adapter.isEnabled)
            listener.onNfcChecked(isNfcAvailable = false, isNfcEnabled = true)
        else
            listener.onNfcChecked(isNfcAvailable = true, isNfcEnabled = true)
    }


}