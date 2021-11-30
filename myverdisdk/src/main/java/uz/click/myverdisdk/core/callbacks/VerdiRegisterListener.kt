package uz.click.myverdisdk.core.callbacks

import java.io.Serializable
import java.lang.Exception

interface VerdiRegisterListener : Serializable {
    fun onRegisterError(exception: Exception)
    fun onRegisterSuccess()
}