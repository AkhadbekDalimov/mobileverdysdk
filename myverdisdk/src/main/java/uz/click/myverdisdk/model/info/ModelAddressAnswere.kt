package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.click.myverdisdk.core.Verdi
import uz.click.myverdisdk.model.request.Answere

class ModelAddressAnswere() : Parcelable {
    var answere: Answere? = null

    @field:Json(name = "Address")
    var modelAddress: ModelAddress? = null

    constructor(parcel: Parcel) : this() {
        modelAddress = parcel.readParcelable(ModelAddress::class.java.classLoader)
    }

    override fun toString(): String {
        return "ModelAddressAnswere{" +
                "answere=" + answere +
                ", modelAddress=" + modelAddress +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(modelAddress, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getAddressPairList(): List<Pair<String, String>> {
        val addressPairList = ArrayList<Pair<String, String>>()
        val fields = ModelAddress::class.java.declaredFields
        fields.forEach { field ->
            field.isAccessible = true
            try {
                val value = field.get(Verdi.result.address?.modelAddress)
                if (value != null && value != "") {
                    addressPairList.add(Pair(field.name, value.toString()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return addressPairList
    }

    companion object CREATOR : Parcelable.Creator<ModelAddressAnswere> {
        override fun createFromParcel(parcel: Parcel): ModelAddressAnswere {
            return ModelAddressAnswere(parcel)
        }

        override fun newArray(size: Int): Array<ModelAddressAnswere?> {
            return arrayOfNulls(size)
        }
    }
}