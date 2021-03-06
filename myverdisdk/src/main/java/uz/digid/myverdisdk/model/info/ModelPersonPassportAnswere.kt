package uz.digid.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.digid.myverdisdk.model.request.Answer

class ModelPersonPassportAnswere() : Parcelable {
    private var answer: Answer? = null

    @field:Json(name = "PersonPassport")
     var personPassport: ModelPersonPassport? = null

    constructor(parcel: Parcel) : this() {
        personPassport = parcel.readParcelable(ModelPersonPassport::class.java.classLoader)
    }

    override fun toString(): String {
        return "ModelPersonPassportAnswere{" +
                "answere=" + answer +
                ", personPassport=" + personPassport +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(personPassport, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelPersonPassportAnswere> {
        override fun createFromParcel(parcel: Parcel): ModelPersonPassportAnswere {
            return ModelPersonPassportAnswere(parcel)
        }

        override fun newArray(size: Int): Array<ModelPersonPassportAnswere?> {
            return arrayOfNulls(size)
        }
    }
}