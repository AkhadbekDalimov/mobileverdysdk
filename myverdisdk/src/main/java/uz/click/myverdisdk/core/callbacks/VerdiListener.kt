package uz.click.myverdisdk.core.callbacks

import java.lang.Exception

/**
 * It was used in Verification process and Document Scanning process
 * @author Azamat
 */
interface VerdiListener {
    /**
     * Called When SDK methods successfully passes the given requirements.
     */
    fun onSuccess()

    /**
     * Called with exception When SDK methods fails the given requirements.
     */
    fun onError(exception: Exception)
}