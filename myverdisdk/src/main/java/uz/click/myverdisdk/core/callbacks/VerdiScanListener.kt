package uz.click.myverdisdk.core.callbacks

import java.io.Serializable

/**
 * @author Azamat Makhkamov
 *
 * Triggered when the Scanner successfully reads the document.
 * The read document will be assigned to the config object [uz.click.myverdisdk.core.VerdiUserConfig]
 * in [uz.click.myverdisdk.core.VerdiUser]
 */
interface VerdiScanListener : Serializable {
    fun onScanSuccess()
}