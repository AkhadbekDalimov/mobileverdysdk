package uz.digid.myverdisdk.model.response

import com.squareup.moshi.Json

data class LivenessScore (
    @field:Json(name = "code")
    val code : Int,
    @field:Json(name = "liveness")
    val liveness: Double
)