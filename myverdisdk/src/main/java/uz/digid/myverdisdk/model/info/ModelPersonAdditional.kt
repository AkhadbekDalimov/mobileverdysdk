package uz.digid.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json



class ModelPersonAdditional() : Parcelable {
    @field:Json(name = "Inn")
    var inn: String? = null

    @field:Json(name = "InnDate")
    var innDate: String? = null

    @field:Json(name = "TaxCode")
    var taxCode: String? = null

    @field:Json(name = "TaxName")
    var taxName: String? = null

    @field:Json(name = "Inps")
    var inps: String? = null

    @field:Json(name = "InpsDate")
    var inpsDate: String? = null

    @field:Json(name = "InpsDocument")
    var inpsDocument: String? = null

    @field:Json(name = "InpsIssuedBy")
    var inpsIssuedBy: String? = null

    @field:Json(name = "Additional")
    var additional: String? = null

    constructor(parcel: Parcel) : this() {
        inn = parcel.readString()
        innDate = parcel.readString()
        taxCode = parcel.readString()
        taxName = parcel.readString()
        inps = parcel.readString()
        inpsDate = parcel.readString()
        inpsDocument = parcel.readString()
        inpsIssuedBy = parcel.readString()
        additional = parcel.readString()
    }

    override fun toString(): String {
        return "ModelPersonAdditional{" +
                "inn='" + inn + '\'' +
                ", innDate='" + innDate + '\'' +
                ", taxCode='" + taxCode + '\'' +
                ", taxName='" + taxName + '\'' +
                ", inps='" + inps + '\'' +
                ", inpsDate='" + inpsDate + '\'' +
                ", inpsDocument='" + inpsDocument + '\'' +
                ", inpsIssuedBy='" + inpsIssuedBy + '\'' +
                ", additional='" + additional + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(inn)
        parcel.writeString(innDate)
        parcel.writeString(taxCode)
        parcel.writeString(taxName)
        parcel.writeString(inps)
        parcel.writeString(inpsDate)
        parcel.writeString(inpsDocument)
        parcel.writeString(inpsIssuedBy)
        parcel.writeString(additional)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelPersonAdditional> {
        override fun createFromParcel(parcel: Parcel): ModelPersonAdditional {
            return ModelPersonAdditional(parcel)
        }

        override fun newArray(size: Int): Array<ModelPersonAdditional?> {
            return arrayOfNulls(size)
        }
    }
}