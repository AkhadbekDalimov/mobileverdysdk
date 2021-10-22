package uz.click.myverdisdk.core.callbacks

import android.graphics.Bitmap
import java.io.Serializable

/**
 * @author Azamat Makhkamov
 *
 * Triggered when the Selfie successfully taken.
 * The image taken info is saved to the config object [uz.click.myverdisdk.core.VerdiUserConfig]
 * in [uz.click.myverdisdk.core.VerdiUser]
 */
interface VerdiSelfieListener : Serializable  {
    fun onSelfieSuccess(selfie : Bitmap)
}