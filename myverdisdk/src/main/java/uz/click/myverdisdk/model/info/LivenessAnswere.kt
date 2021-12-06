package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.click.myverdisdk.model.request.Answer


class LivenessAnswere() : Parcelable {

    @field:Json(name = "answere")
    private val answer: Answer? = null
    @field:Json(name  = "validateResponse",
//        alternate = ["ValidateResponse"]
    )
    val validateResponse: ValidateResponse? = null

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LivenessAnswere> {
        override fun createFromParcel(parcel: Parcel): LivenessAnswere {
            return LivenessAnswere(parcel)
        }

        override fun newArray(size: Int): Array<LivenessAnswere?> {
            return arrayOfNulls(size)
        }
    }

}