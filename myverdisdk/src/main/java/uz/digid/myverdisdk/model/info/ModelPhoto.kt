package uz.digid.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.digid.myverdisdk.model.request.Answer

class ModelPhoto() : Parcelable {
    @field:Json(name = "Answere")
     var answer: Answer? = null

    @field:Json(name = "PersonPhoto")
    var personPhoto: String? = null

    @field:Json(name = "Additional")
    var additional: String? = null

    constructor(parcel: Parcel) : this() {
        personPhoto = parcel.readString()
        additional = parcel.readString()
    }

    override fun toString(): String {
        return "ModelPhoto{" +
                "answere=" + answer +
                ", personPhoto='" + personPhoto + '\'' +
                ", additional='" + additional + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(personPhoto)
        parcel.writeString(additional)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelPhoto> {
        override fun createFromParcel(parcel: Parcel): ModelPhoto {
            return ModelPhoto(parcel)
        }

        override fun newArray(size: Int): Array<ModelPhoto?> {
            return arrayOfNulls(size)
        }
    }
}