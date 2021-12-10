package uz.digid.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

class ModelPersonPhoto() : Parcelable {
  /*  @field:Json(name = "Answere")
     var answere: Answere? = null*/

    @field:Json(name = "PersonPhoto")
    var personPhoto: String? = null

    @field:Json(name = "Additional")
    var additional: String? = null

    constructor(parcel: Parcel) : this() {
        personPhoto = parcel.readString()
        additional = parcel.readString()
    }


    override fun toString(): String {
        return "ModelPersonPhoto{" +
             //   "answere=" + answere +
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

    companion object CREATOR : Parcelable.Creator<ModelPersonPhoto> {
        override fun createFromParcel(parcel: Parcel): ModelPersonPhoto {
            return ModelPersonPhoto(parcel)
        }

        override fun newArray(size: Int): Array<ModelPersonPhoto?> {
            return arrayOfNulls(size)
        }
    }
}