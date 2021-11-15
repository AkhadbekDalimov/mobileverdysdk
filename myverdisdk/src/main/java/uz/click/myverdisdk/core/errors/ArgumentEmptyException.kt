package uz.click.myverdisdk.core.errors


class ArgumentEmptyException(val msg : String) : Exception(){
    override val message: String?
        get() = msg
}