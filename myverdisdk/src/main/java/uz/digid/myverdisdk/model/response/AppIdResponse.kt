package uz.digid.myverdisdk.model.response

data class AppIdResponse(
    val code: Int,
    val message: String = "Successful",
    val response: AppIdResponseMessage? = AppIdResponseMessage()

)

data class AppIdResponseMessage(
    var message: Boolean = false,
    var reqGUID: String = ""
)
