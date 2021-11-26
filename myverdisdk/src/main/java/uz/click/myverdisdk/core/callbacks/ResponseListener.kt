package uz.click.myverdisdk.core.callbacks

/**
 * @author Azamat on 26/11/21
 * */
interface ResponseListener<T> {
    fun onFailure(e: Exception)
    fun onSuccess(response: T)
}