package uz.digid.myverdisdk.core.callbacks

import java.io.Serializable
import java.lang.Exception

/**
 * It was used in Registration process
 * @author Azamat
 */
interface VerdiRegisterListener : Serializable {
    /**
     * Called When SDK methods successfully passes the given requirements.
     */
    fun onRegisterSuccess()

    /**
     * Called with exception When SDK methods fails the given requirements.
     */
    fun onRegisterError(exception: Exception)
}