package uz.digid.myverdisdk.util

sealed class DocumentInputType {
    data class PASSPORT(val text: String) : DocumentInputType()
    data class BIRTHDAY(val text: String) : DocumentInputType()
    data class EXPIRATION(val text: String) : DocumentInputType()
}

