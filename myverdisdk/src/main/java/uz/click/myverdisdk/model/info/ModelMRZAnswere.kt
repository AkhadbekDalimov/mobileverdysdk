package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.click.myverdisdk.model.request.Answer

class ModelMRZAnswere() : Parcelable {
    private var answer: Answer? = null

    @field:Json(name = "MRZ")
     var mrz: ModelMRZ? = null

    constructor(parcel: Parcel) : this() {
        mrz = parcel.readParcelable(ModelMRZ::class.java.classLoader)
    }

    fun getAnswere(): Answer? {
        return answer
    }


    override fun toString(): String {
        return "ModelMRZAnswere{" +
                "answere=" + answer +
                ", mrz=" + mrz +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(mrz, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelMRZAnswere> {
        override fun createFromParcel(parcel: Parcel): ModelMRZAnswere {
            return ModelMRZAnswere(parcel)
        }

        override fun newArray(size: Int): Array<ModelMRZAnswere?> {
            return arrayOfNulls(size)
        }
    }
}