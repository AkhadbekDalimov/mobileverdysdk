package uz.digid.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class ModelPersonPassport(
    @field:Json(name = "Name")
    var name: String? = null,

    @field:Json(name = "Surname")
    var surname: String? = null,

    @field:Json(name = "BirthDate")
    var birthDate: String? = null,

    @field:Json(name = "Nationality")
    var nationality: String? = null,

    @field:Json(name = "Sex")
    var sex: String? = null,

    @field:Json(name = "ExpiryDate")
    var expiryDate: String? = null,

    @field:Json(name = "DocumentNumber")
    var documentNumber: String? = null,

    @field:Json(name = "DocumentType")
    var documentType: String? = null,

    @field:Json(name = "Issuer")
    var issuer: String? = null,

    @field:Json(name = "Pinpp")
    var pinpp: String? = null,

    @field:Json(name = "Additional")
    var additional: String? = null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(birthDate)
        parcel.writeString(nationality)
        parcel.writeString(sex)
        parcel.writeString(expiryDate)
        parcel.writeString(documentNumber)
        parcel.writeString(documentType)
        parcel.writeString(issuer)
        parcel.writeString(pinpp)
        parcel.writeString(additional)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelPersonPassport> {
        override fun createFromParcel(parcel: Parcel): ModelPersonPassport {
            return ModelPersonPassport(parcel)
        }

        override fun newArray(size: Int): Array<ModelPersonPassport?> {
            return arrayOfNulls(size)
        }
    }
}