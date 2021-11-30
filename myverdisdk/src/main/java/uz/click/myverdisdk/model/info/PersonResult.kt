package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.click.myverdisdk.core.Verdi
import uz.click.myverdisdk.model.request.Answere
import uz.click.myverdisdk.data.model.response.ClientResponse

class PersonResult() : Parcelable {
    @field:Json(name = "answere")
    val answer: Answere? = null

    @field:Json(name = "DocumentReadingTime")
    val documentReadingTime: Long = 0

    @field:Json(name = "ServiceAnswereTime")
    val serviceAnswerTime: Long = 0

    @field:Json(name = "RequestGuid")
    val requestGuid: String? = null

    @field:Json(name = "ModelServiceInfo")
    val modelServiceInfo: ModelServiceInfo? = null

    @field:Json(name = "Person")
    var person: ModelPerson? = null

    @field:Json(name = "Address")
    val address: ModelAddressAnswere? = null

    @field:Json(name = "AddressTemporary")
    val addressTemporary: ModelAddressTemproaryAnswere? = null

    @field:Json(name = "Contacts")
    val contacts: ModelContactAnswere? = null

    @field:Json(name = "Additional")
    val additional: ModelPersonAdditional? = null

    @field:Json(name = "ModelMRZ")
    val modelMRZ: ModelMRZAnswere? = null

    @field:Json(name = "ModelPersonPassport")
    var modelPersonPassport: ModelPersonPassportAnswere? = null

    @field:Json(name = "ModelPersonIdCard")
    val modelPersonIdCard: ModelPersonIdCardAnswere? = null

    @field:Json(name = "ModelPersonPhoto")
    var modelPersonPhoto: ModelPersonPhoto? = null

    @field:Json(name = "LivenessAnswere")
    val livenessAnswer: LivenessAnswere? = null

    @field:Json(name = "SignString")
    val signString: String? = null

    @field:Json(name = "ClientData")
    val clientData: ClientResponse? = null

    constructor(parcel: Parcel) : this() {
        person = parcel.readParcelable(ModelPerson::class.java.classLoader)
        modelPersonPassport =
            parcel.readParcelable(ModelPersonPassportAnswere::class.java.classLoader)
        modelPersonPhoto = parcel.readParcelable(ModelPersonPhoto::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(person, flags)
        parcel.writeParcelable(modelPersonPassport, flags)
        parcel.writeParcelable(modelPersonPhoto, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonResult> {


        override fun createFromParcel(parcel: Parcel): PersonResult {
            return PersonResult(parcel)
        }

        override fun newArray(size: Int): Array<PersonResult?> {
            return arrayOfNulls(size)
        }
    }


}