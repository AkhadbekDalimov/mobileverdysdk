package uz.digid.myverdisdk.core.errors

class PassportInfoInvalidException : Exception() {
    override val message: String
        get() = "PassportInfoInvalidException"
}