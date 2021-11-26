package uz.click.myverdisdk.core.callbacks

import java.io.Serializable
import java.lang.Exception

interface VerdiNfcListener : Serializable {
    fun onNfcChecked(isNfcAvailable : Boolean, isNfcEnabled : Boolean)
    fun onNfcError(exception: Exception)
    fun onNfcScanned()
}