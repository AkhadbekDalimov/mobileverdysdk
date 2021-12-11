package uz.digid.myverdisdk.model

import android.graphics.Bitmap

data class FinalResult(
    var livenessScore: Double = 0.0,
    var similarityScore: Double = 0.0,
    var passportPhoto: Bitmap? = null,
    var selfiePhoto: Bitmap? = null,
    var personPairList : List<Pair<String, String>>? = ArrayList(),
    var addressPairList : List<Pair<String, String>>? = ArrayList()
)