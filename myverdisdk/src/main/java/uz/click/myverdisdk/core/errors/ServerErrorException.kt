package uz.click.myverdisdk.core.errors

/**
 * @author rahmatkhujaevs on 30/01/19
 * */
class ServerErrorException(var code: Int?, var msg: String?) : Exception(){
    override val message: String?
        get() = msg
}