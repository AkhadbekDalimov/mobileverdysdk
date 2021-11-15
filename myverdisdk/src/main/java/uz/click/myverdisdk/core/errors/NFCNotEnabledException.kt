package uz.click.myverdisdk.core.errors

import java.lang.Exception

class NFCNotEnabledException(private val msg : String) : Exception(){
    override val message: String
        get() = msg
}