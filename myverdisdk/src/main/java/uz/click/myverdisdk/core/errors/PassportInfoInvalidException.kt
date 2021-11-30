package uz.click.myverdisdk.core.errors

class PassportInfoInvalidException : Exception() {
    override val message: String
        get() = "PassportInfoInvalidException"
}