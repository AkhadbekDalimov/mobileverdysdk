package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json


class ModelContact():Parcelable {
    @field:Json(name = "ZipCode")
    val zipCode: String? = null

    @field:Json(name = "Phone")
    val phone: String? = null

    @field:Json(name = "MobilePhone")
    val mobilePhone: String? = null

    @field:Json(name = "Fax")
    val fax: String? = null

    @field:Json(name = "Email")
    val email: String? = null

    @field:Json(name = "Web")
    val web: String? = null

    @field:Json(name = "Additional")
    val additional: String? = null

    constructor(parcel: Parcel) : this() {
    }

    override fun toString(): String {
        return "ModelContact{" +
                "zipCode='" + zipCode + '\'' +
                ", phone='" + phone + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", web='" + web + '\'' +
                ", additional='" + additional + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelContact> {
        override fun createFromParcel(parcel: Parcel): ModelContact {
            return ModelContact(parcel)
        }

        override fun newArray(size: Int): Array<ModelContact?> {
            return arrayOfNulls(size)
        }
    }


}