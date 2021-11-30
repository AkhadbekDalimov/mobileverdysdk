package uz.click.myverdisdk.model.request

import uz.click.myverdisdk.model.info.PersonResult

class RegistrationResponse(
    val code: Int,
    val message: String = "Successful",
    val response: PersonResult? = null
)

