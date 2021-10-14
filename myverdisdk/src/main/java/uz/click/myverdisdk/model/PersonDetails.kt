package uz.click.myverdisdk.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class PersonDetails() : Parcelable {
    var docType: String? = DocType.PASSPORT
    var name: String? = null
    var surname: String? = null
    var personalNumber: String? = null
    var gender: String? = null
    var birthDate: String? = null
    var expiryDate: String? = null
    var serialNumber: String? = null
    var nationality: String? = null
    var imageFaceBase: Bitmap? = null
    var email: String?= null
    var phoneNumber: String?= null
    var faceImageBase64: String?= null
    var faceImage: Bitmap?= null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        surname = parcel.readString()
        personalNumber = parcel.readString()
        gender = parcel.readString()
        birthDate = parcel.readString()
        expiryDate = parcel.readString()
        serialNumber = parcel.readString()
        nationality = parcel.readString()
        imageFaceBase = parcel.readParcelable(Bitmap::class.java.classLoader)
        email = parcel.readString()
        phoneNumber = parcel.readString()
        docType = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(personalNumber)
        parcel.writeString(gender)
        parcel.writeString(birthDate)
        parcel.writeString(expiryDate)
        parcel.writeString(serialNumber)
        parcel.writeString(nationality)
        parcel.writeParcelable(imageFaceBase, flags)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeString(docType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonDetails> {
        override fun createFromParcel(parcel: Parcel): PersonDetails {
            return PersonDetails(parcel)
        }

        override fun newArray(size: Int): Array<PersonDetails?> {
            return arrayOfNulls(size)
        }
    }
}