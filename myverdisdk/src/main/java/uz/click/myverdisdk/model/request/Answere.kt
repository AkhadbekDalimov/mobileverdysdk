package uz.click.myverdisdk.model.request

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json


data class Answere(
    @field:Json(name = "AnswereId")
    val answereId: Int = 0,
    @field:Json(name = "AnswereComment")
    val answereComment: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(answereId)
        parcel.writeString(answereComment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Answere> {
        override fun createFromParcel(parcel: Parcel): Answere {
            return Answere(parcel)
        }

        override fun newArray(size: Int): Array<Answere?> {
            return arrayOfNulls(size)
        }
    }
}