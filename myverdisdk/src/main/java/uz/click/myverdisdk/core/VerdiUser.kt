package uz.click.myverdisdk.core

import android.app.Activity
import androidx.annotation.Keep
import uz.click.myverdisdk.core.callbacks.VerdiScanListener
import uz.click.myverdisdk.core.callbacks.VerdiUserListener
import uz.click.myverdisdk.impl.MainDialogFragment
import uz.click.myverdisdk.impl.scan.ScanActivity

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
    fun openPassportScanActivity(activity: Activity, listener: VerdiScanListener) {
        activity.startActivity(ScanActivity.getInstance(activity, listener))
    }

    @[JvmStatic Keep]
    fun openIdCardQrReaderActivity(activity: Activity, listener: VerdiScanListener) {
        activity.startActivity(ScanActivity.getInstance(activity, listener, true))
    }

}