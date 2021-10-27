package uz.click.myverdisdk.model.request

import uz.click.myverdisdk.model.info.ModelServiceInfo

class ModelPersonAnswere(
    val code: Int,
    val message: String = "Successful",
    val response: RegistrationResponse? = null
)

data class RegistrationResponse(
    val requestGuid: String,
    val modelServiceInfo: ModelServiceInfo,
    val modelMobileData: ModelMobileData,
    val signString: String
)