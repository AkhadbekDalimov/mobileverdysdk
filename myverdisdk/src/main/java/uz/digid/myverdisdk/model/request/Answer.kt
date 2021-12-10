package uz.digid.myverdisdk.model.request

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answer(
    @field:Json(name = "AnswereId")
    val answerId: Int = 0,
    @field:Json(name = "AnswereComment")
    val answerComment: String? = null
) : Parcelable