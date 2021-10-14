package uz.click.myverdisdk.model.info


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

class ModelAddressTemproary() : Parcelable {
    @field:Json(name = "Kadastr")
       var kadastr: String? = null

    @field:Json(name = "Country")
       var country: Long = 0

    @field:Json(name = "CountryName")
    var countryName: String? = null

    @field:Json(name = "CountryNameUz")
    var countryNameUz: String? = null

    @field:Json(name = "Region")
      var region: Long = 0

    @field:Json(name = "RegionName")
    var regionName: String? = null

    @field:Json(name = "RegionNameUz")
    var regionNameUz: String? = null

    @field:Json(name = "District")
        var district: Long = 0

    @field:Json(name = "DistrictName")
    var districtName: String? = null

    @field:Json(name = "DistrictNameUz")
    var districtNameUz: String? = null

    @field:Json(name = "Address")
    var address: String? = null

    @field:Json(name = "Home")
    var home: String? = null

    @field:Json(name = "Flat")
    var flat: String? = null

    @field:Json(name = "Block")
     var block: String? = null

    @field:Json(name = "LiveFromDate")
    var liveFromDate: String? = null

    @field:Json(name = "LiveTillDate")
    var liveTillDate: String? = null

    @field:Json(name = "Additional")
    var additional: String? = null

    constructor(parcel: Parcel) : this() {
        kadastr = parcel.readString()
        country = parcel.readLong()
        countryName = parcel.readString()
        countryNameUz = parcel.readString()
        region = parcel.readLong()
        regionName = parcel.readString()
        regionNameUz = parcel.readString()
        district = parcel.readLong()
        districtName = parcel.readString()
        districtNameUz = parcel.readString()
        address = parcel.readString()
        home = parcel.readString()
        flat = parcel.readString()
        block = parcel.readString()
        liveFromDate = parcel.readString()
        liveTillDate = parcel.readString()
        additional = parcel.readString()
    }

    override fun toString(): String {
        return "ModelAddressTemproary{" +
                "kadastr='" + kadastr + '\'' +
                ", country=" + country +
                ", countryName='" + countryName + '\'' +
                ", countryNameUz='" + countryNameUz + '\'' +
                ", region=" + region +
                ", regionName='" + regionName + '\'' +
                ", regionNameUz='" + regionNameUz + '\'' +
                ", district=" + district +
                ", districtName='" + districtName + '\'' +
                ", districtNameUz='" + districtNameUz + '\'' +
                ", address='" + address + '\'' +
                ", home='" + home + '\'' +
                ", flat='" + flat + '\'' +
                ", block='" + block + '\'' +
                ", liveFromDate='" + liveFromDate + '\'' +
                ", liveTillDate='" + liveTillDate + '\'' +
                ", additional='" + additional + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kadastr)
        parcel.writeLong(country)
        parcel.writeString(countryName)
        parcel.writeString(countryNameUz)
        parcel.writeLong(region)
        parcel.writeString(regionName)
        parcel.writeString(regionNameUz)
        parcel.writeLong(district)
        parcel.writeString(districtName)
        parcel.writeString(districtNameUz)
        parcel.writeString(address)
        parcel.writeString(home)
        parcel.writeString(flat)
        parcel.writeString(block)
        parcel.writeString(liveFromDate)
        parcel.writeString(liveTillDate)
        parcel.writeString(additional)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelAddressTemproary> {
        override fun createFromParcel(parcel: Parcel): ModelAddressTemproary {
            return ModelAddressTemproary(parcel)
        }

        override fun newArray(size: Int): Array<ModelAddressTemproary?> {
            return arrayOfNulls(size)
        }
    }
}