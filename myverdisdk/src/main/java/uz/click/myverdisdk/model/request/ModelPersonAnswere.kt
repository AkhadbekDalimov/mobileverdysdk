package uz.click.myverdisdk.model.request

import uz.click.myverdisdk.model.info.ModelServiceInfo

class ModelPersonAnswere (
    val requestGuid: String,
    val modelServiceInfo: ModelServiceInfo,
    val modelMobileData: ModelMobileData,
    val signString: String
        )