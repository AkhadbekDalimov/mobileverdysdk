package uz.digid.myverdisdk.model.request

import uz.digid.myverdisdk.model.info.PersonResult

class RegistrationResponse(
    val code: Int,
    val message: String = "Successful",
    val response: PersonResult? = null
)

