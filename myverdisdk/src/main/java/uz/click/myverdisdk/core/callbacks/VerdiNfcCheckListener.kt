package uz.click.myverdisdk.core.callbacks

import java.io.Serializable

interface VerdiNfcCheckListener : Serializable {
    fun onNfcChecked(isNfcAvailable : Boolean, isNfcEnabled : Boolean)
}