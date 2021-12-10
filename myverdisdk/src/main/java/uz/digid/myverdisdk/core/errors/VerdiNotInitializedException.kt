package uz.digid.myverdisdk.core.errors

class VerdiNotInitializedException : Exception() {
    override val message: String
        get() = "Verdi is not initialized, please initialize it!"
}