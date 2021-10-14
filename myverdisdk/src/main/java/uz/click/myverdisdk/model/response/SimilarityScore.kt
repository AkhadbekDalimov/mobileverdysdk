package uz.click.myverdisdk.model.response

import com.squareup.moshi.Json

data class SimilarityScore (
    @field:Json(name = "code")
    var code : Int,
    @field:Json(name = "similarity")
    val similarity: Double
)