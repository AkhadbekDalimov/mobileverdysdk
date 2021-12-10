package uz.digid.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.digid.myverdisdk.model.request.Answer


class ModelAddressTemproaryAnswere() : Parcelable {
     var answer: Answer? = null

    @field:Json(name = "AddressTemproary")
     var addressTemproary: List<ModelAddressTemproary>? = null

    constructor(parcel: Parcel) : this() {
        addressTemproary = parcel.createTypedArrayList(ModelAddressTemproary)
    }


    override fun toString(): String {
        return "ModelAddressTemproaryAnswere{" +
                "answere=" + answer +
                ", addressTemproary=" + addressTemproary +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(addressTemproary)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelAddressTemproaryAnswere> {
        override fun createFromParcel(parcel: Parcel): ModelAddressTemproaryAnswere {
            return ModelAddressTemproaryAnswere(parcel)
        }

        override fun newArray(size: Int): Array<ModelAddressTemproaryAnswere?> {
            return arrayOfNulls(size)
        }
    }
}