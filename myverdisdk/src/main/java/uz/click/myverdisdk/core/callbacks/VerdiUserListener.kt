package uz.click.myverdisdk.core.callbacks

import java.io.Serializable

interface VerdiUserListener : Serializable{
    fun onSuccess()
    fun onError(message : String)
    fun onScanSuccess()
}