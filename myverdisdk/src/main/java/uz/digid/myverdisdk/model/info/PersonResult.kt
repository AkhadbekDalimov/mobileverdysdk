package uz.digid.myverdisdk.model.info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uz.digid.myverdisdk.model.request.Answer
import uz.digid.myverdisdk.model.response.ClientResponse

@Parcelize
data class PersonResult(
    @field:Json(name = "answere")
    val answer: Answer? = null,

    @field:Json(name = "DocumentReadingTime")
    val documentReadingTime: Long = 0,

    @field:Json(name = "ServiceAnswereTime")
    val serviceAnswerTime: Long = 0,

    @field:Json(name = "RequestGuid")
    val requestGuid: String? = null,

    @field:Json(name = "ModelServiceInfo")
    val modelServiceInfo: ModelServiceInfo? = null,

    @field:Json(name = "Person")
    var person: ModelPerson? = null,

    @field:Json(name = "Address")
    val address: ModelAddressAnswere? = null,

    @field:Json(name = "AddressTemporary")
    val addressTemporary: ModelAddressTemproaryAnswere? = null,

    @field:Json(name = "Contacts")
    val contacts: ModelContactAnswere? = null,

    @field:Json(name = "Additional")
    val additional: ModelPersonAdditional? = null,

    @field:Json(name = "ModelMRZ")
    val modelMRZ: ModelMRZAnswere? = null,

    @field:Json(name = "ModelPersonPassport")
    var modelPersonPassport: ModelPersonPassportAnswere? = null,

    @field:Json(name = "ModelPersonIdCard")
    val modelPersonIdCard: ModelPersonIdCardAnswere? = null,

    @field:Json(name = "ModelPersonPhoto")
    var modelPersonPhoto: ModelPersonPhoto? = null,

    @field:Json(name = "LivenessAnswere")
    val livenessAnswer: LivenessAnswere? = null,

    @field:Json(name = "SignString")
    val signString: String? = null,

    @field:Json(name = "ClientData")
    val clientData: ClientResponse? = null
) : Parcelable