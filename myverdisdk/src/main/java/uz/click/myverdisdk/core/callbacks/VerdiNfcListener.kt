package uz.click.myverdisdk.core.callbacks

import java.io.Serializable
import java.lang.Exception

interface VerdiNfcListener : Serializable {
    fun onNfcSuccess()
    fun onNfcError(exception: Exception)
}