package uz.click.myverdisdk.model.request

import uz.click.myverdisdk.model.info.ModelServiceInfo

class RegistrationResponse(
    val code: Int,
    val message: String = "Successful",
    val response: RegistrationResponseObject? = null
)

data class RegistrationResponseObject(
    val requestGuid: String,
    val modelServiceInfo: ModelServiceInfo,
    val modelMobileData: ModelMobileData,
    val signString: String
)