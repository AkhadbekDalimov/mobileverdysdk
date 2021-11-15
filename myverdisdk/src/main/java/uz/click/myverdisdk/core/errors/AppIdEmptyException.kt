package uz.click.myverdisdk.core.errors


class AppIdEmptyException(val msg: String? = "AppIdEmptyException") : Exception(){
    override val message: String?
        get() = msg
}