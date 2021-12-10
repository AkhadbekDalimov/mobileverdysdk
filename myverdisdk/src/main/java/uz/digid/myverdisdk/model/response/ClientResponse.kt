package uz.digid.myverdisdk.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ClientResponse(
    val device: DeviceResponse? = null
) : Parcelable