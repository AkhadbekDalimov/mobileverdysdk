package uz.digid.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

class ModelPersonIdCard() : Parcelable {
    @field:Json(name = "Name")
    var name: String? = null

    @field:Json(name = "Surname")
    var surname: String? = null

    @field:Json(name = "BirthDate")
    var birthDate: String? = null

    @field:Json(name = "ExpiryDate")
    var expiryDate: String? = null

    @field:Json(name = "IssuingDate")
    var issuingDate: String? = null

    @field:Json(name = "Issuer")
    var issuer: String? = null

    @field:Json(name = "IssuingCountry")
    var issuingCountry: String? = null

    @field:Json(name = "DocumentNumber")
    var documentNumber: String? = null

    @field:Json(name = "Additional")
    var additional: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        surname = parcel.readString()
        birthDate = parcel.readString()
        expiryDate = parcel.readString()
        issuingDate = parcel.readString()
        issuer = parcel.readString()
        issuingCountry = parcel.readString()
        documentNumber = parcel.readString()
        additional = parcel.readString()
    }

    override fun toString(): String {
        return "ModelPersonIdCard{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", issuingDate='" + issuingDate + '\'' +
                ", issuer='" + issuer + '\'' +
                ", issuingCountry='" + issuingCountry + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", additional='" + additional + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(birthDate)
        parcel.writeString(expiryDate)
        parcel.writeString(issuingDate)
        parcel.writeString(issuer)
        parcel.writeString(issuingCountry)
        parcel.writeString(documentNumber)
        parcel.writeString(additional)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelPersonIdCard> {
        override fun createFromParcel(parcel: Parcel): ModelPersonIdCard {
            return ModelPersonIdCard(parcel)
        }

        override fun newArray(size: Int): Array<ModelPersonIdCard?> {
            return arrayOfNulls(size)
        }
    }
}