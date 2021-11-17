package uz.click.myverdisdk.core

import android.app.Activity
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.core.errors.AppIdEmptyException
import uz.click.myverdisdk.core.errors.ArgumentEmptyException
import uz.click.myverdisdk.core.errors.ServerNotAvailableException
import uz.click.myverdisdk.model.info.ModelServiceInfo
import uz.click.myverdisdk.model.info.ServiceInfo
import uz.click.myverdisdk.model.request.*
import uz.click.myverdisdk.model.response.AppIdResponse
import uz.click.myverdisdk.util.ErrorUtils
import uz.click.myverdisdk.util.md5
import uz.click.myverdisdk.util.toBase64
import java.io.IOException
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class VerdiManager(val context: Context? = null) {

    companion object {
        var logs = true
        private const val CONNECT_TIME_OUT: Long = 10 * 1000 // 10 second
        private const val READ_TIME_OUT: Long = 10 * 1000 // 10 second
        private const val WRITE_TIME_OUT: Long = 10 * 1000 // 10 second
        private val JSON = MediaType.parse("application/json; charset=utf-8")

        const val LANGUAGE = "ru"
        const val VERDI_BASE_URL = "https://api.digid.uz:8080/"
        const val VERDI_BASE_URL_TEST = "https://testapi.digid.uz:8082/"
        const val APP_ID_VERDI = "3f4b68e09a319cf4"
        const val APP_ID_CLICK = "P8g13lFKmXo8TlFO"
        const val REQUEST_CHECK_APP_ID = "digid-service/mobile/data/${LANGUAGE}/checkAppId"
        const val REQUEST_SEND_PHONE = "digid-service/phone/${LANGUAGE}/send"
        const val REQUEST_CHECK_PHONE = "digid-service/phone/${LANGUAGE}/check"
        const val REQUEST_REGISTRATION = "digid-service/pinpp/${LANGUAGE}/registration"
        const val REQUEST_VERIFICATION = "digid-service/pinpp/${LANGUAGE}/verification"

        const val AUTH_CHECK_APP_ID = "Basic dGVzdHJlYWQ6dGVzdHBhc3M="
        const val AUTH_PHONE = "Basic ZGlnaWQ6ZGlnaWQyMDE5"
        const val AUTH_PINPP = "Basic aXBvdGVrYV9tb2JpbGU6UmxtX2lwb3Rla2E="
    }

    private var okClient: OkHttpClient
    private var moshi = Moshi.Builder().build()
    var invoiceCancelled = false

    init {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.dispatcher(dispatcher)
        okhttpClientBuilder.addInterceptor(loggingInterceptor())
        if (context != null)
            okhttpClientBuilder.addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
        okhttpClientBuilder
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
        okClient = okhttpClientBuilder.build()
    }

    private fun loggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        if (logs)
            logging.level = HttpLoggingInterceptor.Level.BODY
        else
            logging.level = HttpLoggingInterceptor.Level.NONE
        return logging
    }

    fun checkAppId(activity: Activity, listener: ResponseListener<AppIdResponse>) {
        if (VerdiUser.config.appId.isEmpty()) {
            listener.onFailure(AppIdEmptyException(activity.getString(R.string.AppIdNotExist)))
            return
        }
        val appIdRequest = AppIdRequest(
            VerdiUser.config.appId,
            UUID.randomUUID().toString()
        )
        val adapter = moshi.adapter<AppIdRequest>(AppIdRequest::class.java)
        val body = RequestBody.create(JSON, adapter.toJson(appIdRequest))
        val request = Request.Builder()
            .url(VERDI_BASE_URL + REQUEST_CHECK_APP_ID)
            .addHeader("Accept", "application/json")
            .addHeader("Content-type", "application/json")
            .addHeader("Authorization", AUTH_CHECK_APP_ID)
            .post(body)
            .build()
        okClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    if (response.body() == null) {
                        listener.onFailure(
                            ServerNotAvailableException(
                                response.code(),
                                response.message()
                            )
                        )
                        return
                    }

                    response.body()?.let {

                        val initialResponse =
                            moshi.adapter<AppIdResponse>(AppIdResponse::class.java)
                                .fromJson(it.string())
                        activity.runOnUiThread {
                            when (initialResponse?.code) {
                                0 -> {
                                    listener.onSuccess(initialResponse)
                                }
                                else -> {
                                    listener.onFailure(
                                        ErrorUtils.getException(
                                            initialResponse?.code,
                                            initialResponse?.message
                                        )
                                    )
                                }
                            }
                        }
                    }

                } else {
                    listener.onFailure(
                        ServerNotAvailableException(response.code(), response.message())
                    )
                }
            }
        })
    }

    fun registerPerson(activity: Activity, listener: ResponseListener<RegistrationResponse>) {
        val config = VerdiUser.config

        val publicKey = UUID.randomUUID().toString()
        val guid = UUID.randomUUID().toString()
        val deviceID: String =
            Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
        val signString = md5(
            guid +
                    config.serialNumber +
                    config.birthDate +
                    config.dateOfExpiry +
                    publicKey
        )
        val passRequest = PassportRequest(
            config.serialNumber,
            config.personalNumber,
            config.docType,
            config.birthDate,
            config.dateOfExpiry
        )
        val modelPersonRequest = ModelPersonRequest(passRequest)
        val answere = Answere(1, "OK")
        val base64Pass = config.imageFaceBase?.toBase64()
        val personPhoto = ModelPersonPhotoRequest(answere, config.base64Image, base64Pass)
        val model = Build.MODEL
        val modelMobileData =
            ModelMobileData(
                model,
                "",
                deviceID,
                ""
            )
        val serviceInfo = ServiceInfo()
        serviceInfo.scannerSerial = deviceID
        val modelServiceInfo = ModelServiceInfo()
        modelServiceInfo.serviceInfo = serviceInfo
        val passportRequest = PassportInfoRequest(
            requestGuid = guid,
            modelPersonPassport = modelPersonRequest,
            modelPersonPhoto = personPhoto,
            modelServiceInfo = modelServiceInfo,
            signString = signString,
            clientPubKey = publicKey,
            modelMobileData = modelMobileData,
            appId = VerdiUser.config.appId
        )
        Log.d("RequestTag",
            moshi.adapter<PassportInfoRequest>(PassportInfoRequest::class.java)
                .toJson(passportRequest)
        )
        val adapter = moshi.adapter<PassportInfoRequest>(PassportInfoRequest::class.java)
        val body = RequestBody.create(JSON, adapter.toJson(passportRequest))
        val request = Request.Builder()
            .url(VERDI_BASE_URL + REQUEST_REGISTRATION)
            .addHeader("Accept", "application/json")
            .addHeader("Content-type", "application/json")
            .addHeader("Authorization", AUTH_PINPP)
            .post(body)
            .build()

        okClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    if (response.body() == null) {
                        listener.onFailure(
                            ServerNotAvailableException(
                                response.code(),
                                response.message()
                            )
                        )
                        return
                    }

                    response.body()?.let {
                        val initialResponse =
                            moshi.adapter<RegistrationResponse>(RegistrationResponse::class.java)
                                .fromJson(it.string())
                        Log.d("TagCheck",
                            moshi.adapter<RegistrationResponse>(RegistrationResponse::class.java)
                                .toJson(initialResponse)
                        )
                        activity.runOnUiThread {
                            when (initialResponse?.code) {
                                0 -> {
                                    listener.onSuccess(initialResponse)
                                }
                                else -> {
                                    listener.onFailure(
                                        ErrorUtils.getException(
                                            initialResponse?.code,
                                            initialResponse?.message
                                        )
                                    )
                                }
                            }
                        }
                    }

                } else {
                    listener.onFailure(
                        ServerNotAvailableException(response.code(), response.message())
                    )
                }
            }
        })

    }


    fun verifyPerson(activity: Activity, listener: ResponseListener<RegistrationResponse>) {
        val deviceSerialNumber: String = VerdiUser.config.serialNumber
        val guid = UUID.randomUUID().toString()
        val deviceID: String? =
            Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
        val personPhoto = ModelPersonPhotoRequest(null, null, "")
        val model = Build.MODEL
        val modelMobileData =
            ModelMobileData(model, null, deviceID, null)
        val serviceInfo = ServiceInfo()
        serviceInfo.scannerSerial = deviceSerialNumber
        val modelServiceInfo = ModelServiceInfo()
        modelServiceInfo.serviceInfo = serviceInfo
        val passportRequest = PassportInfoRequest(
            guid,
            null,
            personPhoto,
            modelServiceInfo,
            "",
            guid,
            modelMobileData
        )

        Log.d("RequestTag",
            moshi.adapter<PassportInfoRequest>(PassportInfoRequest::class.java)
                .toJson(passportRequest)
        )
        val adapter = moshi.adapter<PassportInfoRequest>(PassportInfoRequest::class.java)
        val body = RequestBody.create(JSON, adapter.toJson(passportRequest))
        val request = Request.Builder()
            .url(VERDI_BASE_URL + REQUEST_VERIFICATION)
            .addHeader("Accept", "application/json")
            .addHeader("Content-type", "application/json")
            .addHeader("Authorization", AUTH_PINPP)
            .post(body)
            .build()

        okClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    if (response.body() == null) {
                        listener.onFailure(
                            ServerNotAvailableException(
                                response.code(),
                                response.message()
                            )
                        )
                        return
                    }

                    response.body()?.let {
                        val initialResponse =
                            moshi.adapter<RegistrationResponse>(RegistrationResponse::class.java)
                                .fromJson(it.string())
                        Log.d("TagCheck",
                            moshi.adapter<RegistrationResponse>(RegistrationResponse::class.java)
                                .toJson(initialResponse)
                        )
                        activity.runOnUiThread {
                            when (initialResponse?.code) {
                                0 -> {
                                    listener.onSuccess(initialResponse)
                                }
                                else -> {
                                    listener.onFailure(
                                        ErrorUtils.getException(
                                            initialResponse?.code,
                                            initialResponse?.message
                                        )
                                    )
                                }
                            }
                        }
                    }

                } else {
                    listener.onFailure(
                        ServerNotAvailableException(response.code(), response.message())
                    )
                }
            }
        })
    }
}