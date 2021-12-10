package uz.digid.myverdisdk.core.errors

class PassportInfoEmptyException : Exception(){
    override val message: String
        get() = "PassportInfoEmptyException"

}