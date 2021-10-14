package uz.click.myverdisdk.core

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.squareup.moshi.Moshi
import io.reactivex.Single
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class VerdiManager {

    companion object {
        var logs = false
        private const val CONNECT_TIME_OUT: Long = 10 * 1000 // 10 second
        private const val READ_TIME_OUT: Long = 10 * 1000 // 10 second
        private const val WRITE_TIME_OUT: Long = 10 * 1000 // 10 second
        private val JSON = MediaType.parse("application/json; charset=utf-8")
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

}