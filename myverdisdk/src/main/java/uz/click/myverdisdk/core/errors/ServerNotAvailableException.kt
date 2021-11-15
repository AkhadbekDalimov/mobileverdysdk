package uz.click.myverdisdk.core.errors

/**
 * @author rahmatkhujaevs on 29/01/19
 * */
class ServerNotAvailableException(var code: Int?, var msg: String?) : Exception() {
    override val message: String?
        get() = msg
}