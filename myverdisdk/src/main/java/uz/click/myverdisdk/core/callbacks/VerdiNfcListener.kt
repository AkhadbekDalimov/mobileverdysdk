package uz.click.myverdisdk.core.callbacks

import java.io.Serializable

interface VerdiNfcListener : Serializable {
    fun onNfcSuccess()
}