package uz.digid.myverdisdk.core.errors


class ArgumentEmptyException(val msg : String) : Exception(){
    override val message: String?
        get() = msg
}