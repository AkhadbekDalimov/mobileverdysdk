package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

class ServiceInfo() : Parcelable {
    @field:Json(name = "ScannerSerial")
    var scannerSerial: String? = null

    @field:Json(name = "ProductNumber")
    var productNumber: String? = null

    @field:Json(name = "Version")
    var version: String? = null

    @field:Json(name = "FWVersion")
    private var fWVersion: String? = null

    @field:Json(name = "OCRVersion")
    var ocrVersion: String? = null

    @field:Json(name = "ClientIP")
    var clientIP: String? = null

    @field:Json(name = "ClientMAC")
    var clientMAC: String? = null

    @field:Json(name = "ClientOS")
    var clientOS: String? = null

    @field:Json(name = "ApplicationVersion")
    var applicationVersion: String? = null

    @field:Json(name = "Additional")
    var additional: String? = null

    constructor(parcel: Parcel) : this() {
        scannerSerial = parcel.readString()
        productNumber = parcel.readString()
        version = parcel.readString()
        fWVersion = parcel.readString()
        ocrVersion = parcel.readString()
        clientIP = parcel.readString()
        clientMAC = parcel.readString()
        clientOS = parcel.readString()
        applicationVersion = parcel.readString()
        additional = parcel.readString()
    }


    override fun toString(): String {
        return "ServiceInfo{" +
                "scannerSerial='" + scannerSerial + '\'' +
                ", productNumber='" + productNumber + '\'' +
                ", version='" + version + '\'' +
                ", fWVersion='" + fWVersion + '\'' +
                ", ocrVersion='" + ocrVersion + '\'' +
                ", clientIP='" + clientIP + '\'' +
                ", clientMAC='" + clientMAC + '\'' +
                ", clientOS='" + clientOS + '\'' +
                ", applicationVersion='" + applicationVersion + '\'' +
                ", additional='" + additional + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(scannerSerial)
        parcel.writeString(productNumber)
        parcel.writeString(version)
        parcel.writeString(fWVersion)
        parcel.writeString(ocrVersion)
        parcel.writeString(clientIP)
        parcel.writeString(clientMAC)
        parcel.writeString(clientOS)
        parcel.writeString(applicationVersion)
        parcel.writeString(additional)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServiceInfo> {
        override fun createFromParcel(parcel: Parcel): ServiceInfo {
            return ServiceInfo(parcel)
        }

        override fun newArray(size: Int): Array<ServiceInfo?> {
            return arrayOfNulls(size)
        }
    }
}