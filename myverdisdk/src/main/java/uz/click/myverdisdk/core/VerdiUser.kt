package uz.click.myverdisdk.core

import android.app.Activity
import androidx.annotation.Keep
import uz.click.myverdisdk.impl.nfc.NfcActivity
import uz.click.myverdisdk.impl.scan.ScanActivity
import uz.click.myverdisdk.impl.selfie.SelfieActivity

/**
 * @author Azamat on 27/09/21
 * */
object VerdiUser {

    lateinit var config: VerdiUserConfig

    @[JvmStatic Keep]
    fun init(
        config: VerdiUserConfig
    ) {
        this.config = config
    }

    @[JvmStatic Keep]
    fun openDocumentScanActivity(activity: Activity) {
        activity.startActivity(ScanActivity.getInstance(activity))
    }

    @[JvmStatic Keep]
    fun openSelfieActivity(activity: Activity) {
        activity.startActivity(SelfieActivity.getInstance(activity))
    }

    @[JvmStatic Keep]
    fun openNfcScanActivity(activity: Activity) {
        activity.startActivity(NfcActivity.getInstance(activity))
    }
}