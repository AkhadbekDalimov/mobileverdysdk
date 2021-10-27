package uz.click.myverdisdk.model.response

data class ResponseData<T>(
    val code: Int,
    val message: String = "Successful",
    val response: T
)