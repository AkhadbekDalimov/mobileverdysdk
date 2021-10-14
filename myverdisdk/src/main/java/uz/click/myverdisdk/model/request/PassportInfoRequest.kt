package uz.click.myverdisdk.model.request

import uz.click.myverdisdk.model.info.ModelServiceInfo

data class PassportInfoRequest(
    val requestGuid: String,
    val modelPersonPassport: ModelPersonRequest?,
    val modelPersonPhoto: ModelPersonPhotoRequest?,
    val modelServiceInfo: ModelServiceInfo,
    val signString: String?,
    val clientPubKey: String,
    val modelMobileData: ModelMobileData,
    val appId: String =
//        MyVerdiSdk.getInstance()?.appId ?:
        ""

)