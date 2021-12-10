package uz.digid.myverdisdk.core.errors


class UnknownErrorException(var code: Int?, var msg: String?) : Exception(){
    override val message: String?
        get() = msg
}