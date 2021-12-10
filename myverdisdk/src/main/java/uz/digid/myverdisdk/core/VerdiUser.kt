package uz.digid.myverdisdk.core

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Azamat on 26/11/21
 **/
@Parcelize
data class VerdiUser(
    var documentNumber: String = "",
    var serialNumber: String = "",
    var dateOfExpiry: String = "",
    var birthDate: String = "",
    var personalNumber: String = "",
    var docType: String = "",
    var imageFaceBase: Bitmap? = null,
    var base64Image: String? = "",
    var deviceId: String = "",
    var scannerSerial: String = ""
) : Parcelable {

}