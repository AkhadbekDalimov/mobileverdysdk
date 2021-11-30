package uz.click.myverdisdk.core.errors

class PassportInfoEmptyException : Exception(){
    override val message: String
        get() = "PassportInfoEmptyException"

}