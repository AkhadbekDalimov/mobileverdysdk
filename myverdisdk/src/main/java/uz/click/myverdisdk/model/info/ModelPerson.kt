package uz.click.myverdisdk.model.info

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import uz.click.myverdisdk.core.Verdi

class ModelPerson() : Parcelable {
    @field:Json(name = "RequestGuid")
    var requestGuid: String? = null

    @field:Json(name = "Inn")
    var inn: String? = null

    @field:Json(name = "Pinpp")
    var pinpp: String? = null

    @field:Json(name = "SurnameC")
    var surnameC: String? = null

    @field:Json(name = "NameC")
    var nameC: String? = null

    @field:Json(name = "PatronymC")
    var patronymC: String? = null

    @field:Json(name = "SurnameL")
    var surnameL: String? = null

    @field:Json(name = "NameL")
    var nameL: String? = null

    @field:Json(name = "PatronymL")
    var patronymL: String? = null

    @field:Json(name = "SurnameE")
    var surnameE: String? = null

    @field:Json(name = "NameE")
    var nameE: String? = null

    @field:Json(name = "BirthDate")
    var birthDate: String? = null

    @field:Json(name = "Sex")
    var sex: String? = null

    @field:Json(name = "SexName")
    var sexName: String? = null

    @field:Json(name = "SexNameUz")
    var sexNameUz: String? = null

    @field:Json(name = "BirthCountry")
    var birthCountry: String? = null

    @field:Json(name = "BirthCountryName")
    var birthCountryName: String? = null

    @field:Json(name = "BirthCountryNameUz")
    var birthCountryNameUz: String? = null

    @field:Json(name = "BirthPlace")
    var birthPlace: String? = null

    @field:Json(name = "Nationality")
    var nationality: String? = null

    @field:Json(name = "NationalityName")
    var nationalityName: String? = null

    @field:Json(name = "NationalityNameUz")
    var nationalityNameUz: String? = null

    @field:Json(name = "DocumentType")
    var documentType: String? = null

    @field:Json(name = "DocumentTypeName")
    var documentTypeName: String? = null

    @field:Json(name = "DocumentTypeNameUz")
    var documentTypeUz: String? = null

    @field:Json(name = "DocumentSerialNumber")
    var documentSerialNumber: String? = null

    @field:Json(name = "DocumentDateIssue")
    var documentDateIssue: String? = null

    @field:Json(name = "DocumentDateValid")
    var documentDateValid: String? = null

    @field:Json(name = "DocumentIssuedBy")
    var documentIssuedBy: String? = null

    @field:Json(name = "PersonStatus")
    var personStatus = 0

    @field:Json(name = "PersonStatusValue")
    var personStatusValue: String? = null

    @field:Json(name = "Citizenship")
    var citizenship: String? = null

    @field:Json(name = "CitizenshipName")
    var citizenshipName: String? = null

    @field:Json(name = "CitizenshipNameUz")
    var citizenshipNameUz: String? = null

    @field:Json(name = "Additional")
    var additional: String? = null

    constructor(parcel: Parcel) : this() {
        requestGuid = parcel.readString()
        inn = parcel.readString()
        pinpp = parcel.readString()
        surnameC = parcel.readString()
        nameC = parcel.readString()
        patronymC = parcel.readString()
        surnameL = parcel.readString()
        nameL = parcel.readString()
        patronymL = parcel.readString()
        surnameE = parcel.readString()
        nameE = parcel.readString()
        birthDate = parcel.readString()
        sex = parcel.readString()
        sexName = parcel.readString()
        sexNameUz = parcel.readString()
        birthCountry = parcel.readString()
        birthCountryName = parcel.readString()
        birthCountryNameUz = parcel.readString()
        birthPlace = parcel.readString()
        nationality = parcel.readString()
        nationalityName = parcel.readString()
        nationalityNameUz = parcel.readString()
        documentType = parcel.readString()
        documentTypeName = parcel.readString()
        documentTypeUz = parcel.readString()
        documentSerialNumber = parcel.readString()
        documentDateIssue = parcel.readString()
        documentDateValid = parcel.readString()
        documentIssuedBy = parcel.readString()
        personStatus = parcel.readInt()
        personStatusValue = parcel.readString()
        citizenship = parcel.readString()
        citizenshipName = parcel.readString()
        citizenshipNameUz = parcel.readString()
        additional = parcel.readString()
    }

    override fun toString(): String {
        return "ModelPerson{" +
                "pinpp='" + pinpp + '\'' +
                ", surnameC='" + surnameC + '\'' +
                ", nameC='" + nameC + '\'' +
                ", patronymC='" + patronymC + '\'' +
                ", surnameL='" + surnameL + '\'' +
                ", nameL='" + nameL + '\'' +
                ", patronymL='" + patronymL + '\'' +
                ", surnameE='" + surnameE + '\'' +
                ", nameE='" + nameE + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", sex='" + sex + '\'' +
                ", sexName='" + sexName + '\'' +
                ", sexNameUz='" + sexNameUz + '\'' +
                ", birthCountry='" + birthCountry + '\'' +
                ", birthCountryName='" + birthCountryName + '\'' +
                ", birthCountryNameUz='" + birthCountryNameUz + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", nationality='" + nationality + '\'' +
                ", nationalityName='" + nationalityName + '\'' +
                ", nationalityNameUz='" + nationalityNameUz + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentTypeName='" + documentTypeName + '\'' +
                ", documentTypeUz='" + documentTypeUz + '\'' +
                ", documentSerialNumber='" + documentSerialNumber + '\'' +
                ", documentDateIssue='" + documentDateIssue + '\'' +
                ", documentDateValid='" + documentDateValid + '\'' +
                ", documentIssuedBy='" + documentIssuedBy + '\'' +
                ", personStatus=" + personStatus +
                ", personStatusValue='" + personStatusValue + '\'' +
                ", citizenship='" + citizenship + '\'' +
                ", citizenshipName='" + citizenshipName + '\'' +
                ", citizenshipNameUz='" + citizenshipNameUz + '\'' +
                ", additional='" + additional + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(requestGuid)
        parcel.writeString(inn)
        parcel.writeString(pinpp)
        parcel.writeString(surnameC)
        parcel.writeString(nameC)
        parcel.writeString(patronymC)
        parcel.writeString(surnameL)
        parcel.writeString(nameL)
        parcel.writeString(patronymL)
        parcel.writeString(surnameE)
        parcel.writeString(nameE)
        parcel.writeString(birthDate)
        parcel.writeString(sex)
        parcel.writeString(sexName)
        parcel.writeString(sexNameUz)
        parcel.writeString(birthCountry)
        parcel.writeString(birthCountryName)
        parcel.writeString(birthCountryNameUz)
        parcel.writeString(birthPlace)
        parcel.writeString(nationality)
        parcel.writeString(nationalityName)
        parcel.writeString(nationalityNameUz)
        parcel.writeString(documentType)
        parcel.writeString(documentTypeName)
        parcel.writeString(documentTypeUz)
        parcel.writeString(documentSerialNumber)
        parcel.writeString(documentDateIssue)
        parcel.writeString(documentDateValid)
        parcel.writeString(documentIssuedBy)
        parcel.writeInt(personStatus)
        parcel.writeString(personStatusValue)
        parcel.writeString(citizenship)
        parcel.writeString(citizenshipName)
        parcel.writeString(citizenshipNameUz)
        parcel.writeString(additional)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getPersonPairList(): List<Pair<String, String>> {
        val pairList = ArrayList<Pair<String, String>>()
        val fields = ModelPerson::class.java.declaredFields
        fields.forEach { field ->
            field.isAccessible = true
            val person = Verdi.result.person
            if (person != null) {
                val value = field.get(person)
                if (value != null && value != "") {
                    pairList.add(Pair(field.name, value.toString()))
                }
            }
        }
        return pairList
    }
    companion object CREATOR : Parcelable.Creator<ModelPerson> {

        override fun createFromParcel(parcel: Parcel): ModelPerson {
            return ModelPerson(parcel)
        }

        override fun newArray(size: Int): Array<ModelPerson?> {
            return arrayOfNulls(size)
        }
    }
}