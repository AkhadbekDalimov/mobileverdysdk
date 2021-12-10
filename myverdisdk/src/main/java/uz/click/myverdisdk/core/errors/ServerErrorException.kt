package uz.click.myverdisdk.core.errors

class ServerErrorException(var code: Int?, var msg: String?) : Exception(){
    override val message: String?
        get() = msg
}