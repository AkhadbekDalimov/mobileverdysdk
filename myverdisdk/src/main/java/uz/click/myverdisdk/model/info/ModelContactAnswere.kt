package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.click.myverdisdk.model.request.Answer


class ModelContactAnswere() : Parcelable {
     var answer: Answer? = null

    @field:Json(name = "Contacts")
     var contacts: List<ModelContact>? = null

    constructor(parcel: Parcel) : this() {
        contacts = parcel.createTypedArrayList(ModelContact)
    }

    override fun toString(): String {
        return "ModelContactAnswere{" +
                "answere=" + answer +
                ", contacts=" + contacts +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(contacts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelContactAnswere> {
        override fun createFromParcel(parcel: Parcel): ModelContactAnswere {
            return ModelContactAnswere(parcel)
        }

        override fun newArray(size: Int): Array<ModelContactAnswere?> {
            return arrayOfNulls(size)
        }
    }
}