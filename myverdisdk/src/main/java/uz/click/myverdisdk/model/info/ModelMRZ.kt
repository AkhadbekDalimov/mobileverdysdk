package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json


class ModelMRZ() : Parcelable {
    @field:Json(name = "Line1")
    var line1: String? = null

    @field:Json(name = "Line2")
    var line2: String? = null

    @field:Json(name = "Line3")
    var line3: String? = null

    @field:Json(name = "Additional")
    var additional: String? = null

    constructor(parcel: Parcel) : this() {
        line1 = parcel.readString()
        line2 = parcel.readString()
        line3 = parcel.readString()
        additional = parcel.readString()
    }

    override fun toString(): String {
        return "ModelMRZ{" +
                "line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", line3='" + line3 + '\'' +
                ", additional='" + additional + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(line1)
        parcel.writeString(line2)
        parcel.writeString(line3)
        parcel.writeString(additional)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelMRZ> {
        override fun createFromParcel(parcel: Parcel): ModelMRZ {
            return ModelMRZ(parcel)
        }

        override fun newArray(size: Int): Array<ModelMRZ?> {
            return arrayOfNulls(size)
        }
    }
}