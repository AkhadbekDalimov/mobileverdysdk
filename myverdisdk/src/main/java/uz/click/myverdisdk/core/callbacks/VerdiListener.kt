package uz.click.myverdisdk.core.callbacks

import java.lang.Exception

interface VerdiListener {
    fun onSuccess()
    fun onError(exception: Exception)
}