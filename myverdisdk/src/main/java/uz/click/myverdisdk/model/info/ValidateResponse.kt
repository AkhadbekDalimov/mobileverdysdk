package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.click.myverdisdk.model.response.LivenessScore
import uz.click.myverdisdk.model.response.SimilarityScore

data class ValidateResponse(
    @field:Json(name = "code")
    val code: Int,
    @field:Json(name = "livenessScore")
    val livenessScore: LivenessScore,
    @field:Json(name  = "similarityScore",
//        alternate = ["SimilarityScore"]
    )
    val similarityScore: SimilarityScore? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        TODO("livenessScore"),
        TODO("similarityScore")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ValidateResponse> {
        override fun createFromParcel(parcel: Parcel): ValidateResponse {
            return ValidateResponse(parcel)
        }

        override fun newArray(size: Int): Array<ValidateResponse?> {
            return arrayOfNulls(size)
        }
    }
}