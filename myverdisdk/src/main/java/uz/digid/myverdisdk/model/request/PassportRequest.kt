package uz.digid.myverdisdk.model.request

data class PassportRequest (

    var documentNumber: String? = null,

    var pinpp: String? = null,

    var documentType: String? = null,

    var birthDate: String? = null,

    var expiryDate: String? = null
)