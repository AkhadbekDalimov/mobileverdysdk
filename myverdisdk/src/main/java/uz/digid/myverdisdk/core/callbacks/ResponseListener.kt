package uz.digid.myverdisdk.core.callbacks

/**
 * @author Azamat
 */
interface ResponseListener<T> {
    /**
     * Called when the request could not be executed due to cancellation, a connectivity problem or
     * timeout. Because networks can fail during an exchange, it is possible that the remote server
     * accepted the request before the failure.
     */
    fun onFailure(e: Exception)

    /**
     * Called when the HTTP response was successfully returned by the remote server. The callback may
     * proceed to read the response body with {@link Response#body}.The recipient of the callback may
     * consume the response body on the main thread (It was handled by SDK Requests).
     */
    fun onSuccess(response: T)
}