package uz.digid.myverdisdk.model.request

import uz.digid.myverdisdk.model.info.ModelServiceInfo

data class PassportInfoRequest(
    val appId: String = "",
    val requestGuid: String,
    val signString: String?,
    val clientPubKey: String,
    val modelPersonPassport: ModelPersonRequest?,
    val modelPersonPhoto: ModelPersonPhotoRequest?,
    val modelServiceInfo: ModelServiceInfo,
    val modelMobileData: ModelMobileData
)