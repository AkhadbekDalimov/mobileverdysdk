package uz.click.myverdisdk.core.callbacks

import java.io.Serializable
import java.lang.Exception

interface VerdiNfcListener : Serializable {
    fun onNfcError(exception: Exception)
    fun onNfcScanned()
}