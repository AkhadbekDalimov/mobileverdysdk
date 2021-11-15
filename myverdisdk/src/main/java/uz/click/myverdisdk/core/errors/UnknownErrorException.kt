package uz.click.myverdisdk.core.errors


class UnknownErrorException(var code: Int?, var msg: String?) : Exception(){
    override val message: String?
        get() = msg
}