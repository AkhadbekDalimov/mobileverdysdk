package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.click.myverdisdk.model.request.Answer

class ModelPersonIdCardAnswere() : Parcelable {
    private var answer: Answer? = null

    @field:Json(name = "PersonIdCard")
     var personIdCard: ModelPersonIdCard? = null

    constructor(parcel: Parcel) : this() {
        personIdCard = parcel.readParcelable(ModelPersonIdCard::class.java.classLoader)
    }

    override fun toString(): String {
        return "ModelPersonIdCardAnswere{" +
                "answere=" + answer +
                ", personIdCard=" + personIdCard +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(personIdCard, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelPersonIdCardAnswere> {
        override fun createFromParcel(parcel: Parcel): ModelPersonIdCardAnswere {
            return ModelPersonIdCardAnswere(parcel)
        }

        override fun newArray(size: Int): Array<ModelPersonIdCardAnswere?> {
            return arrayOfNulls(size)
        }
    }
}