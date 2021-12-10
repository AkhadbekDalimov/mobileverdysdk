package uz.digid.myverdisdk.model.response

data class AuthorizationResponse(
    val livenessScore: LivenessScore?,
    val similarityScore: SimilarityScore?
)