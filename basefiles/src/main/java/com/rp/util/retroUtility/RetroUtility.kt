package dev.rahul.baseutils.v1.util.retroUtility

import android.util.Log
import android.webkit.MimeTypeMap
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

const val TAG: String = "RetroUtility"
const val MB: Long = 1024 *1024


object RetroUtility {

    var MAX_AGE: Int = 60 //in seconds
    var MAX_STALE: Int = 24 * 60 * 60 //in seconds

    var DISK_CACHE_SIZE: Long = 10 //in megabite

    var utility: IRetroUtility? = null

    fun init(iRetroUtility: IRetroUtility) {
        utility = iRetroUtility
    }
}

fun Long.toMB(): Long = this * MB

private val okHttpClientInit: OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            var request = chain.request()

            if (request == null)
                Log.e(TAG, "request is null")

            request = request?.let {
                val newBuilder = it.newBuilder()
                val cacheHeader = it.header("Cache-Control")
                if (cacheHeader == null) {
                    if (RetroUtility.utility?.isConnected() == true) {
                        Log.e(TAG, "is connected")
                        newBuilder.addHeader("Cache-Control", "public, max-age=" + RetroUtility.MAX_AGE)
                    } else {
                        Log.e(TAG, "is not connected")
                        newBuilder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + RetroUtility.MAX_STALE)
                    }
                }
                newBuilder.build()
            }
            chain.proceed(request)
        }
        .build()

fun <T> Class<T>.createRetroService(
        url: String,
        httpLoggerInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        },
        okHttpClient: OkHttpClient = okHttpClientInit
): T {


    val newBuilder = okHttpClient.newBuilder()
    RetroUtility.utility?.getCacheDirectory()?.let {
        Log.e(TAG, "cacheDir let is called")
        newBuilder.cache(Cache(it, RetroUtility.DISK_CACHE_SIZE.toMB()))
    }
    newBuilder.addInterceptor(httpLoggerInterceptor)
    //newBuilder.addInterceptor(httpLoggerInterceptor)

    val build = Retrofit.Builder()
            .client(newBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(url)
            .build()

    return build.create(this)
}

fun File.toMultiPart(key: String): MultipartBody.Part {
    // create RequestBody instance from file
    val requestFile = this.toRequestFile()

    // MultipartBody.Part is used to send also the actual file name
    return MultipartBody.Part.createFormData(key, this.name, requestFile)
}

fun File.toRequestFile(): RequestBody {

    val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
    val mimeType = mimeTypeMap.getMimeTypeFromExtension(this.extension)

    return RequestBody.create(MediaType.parse(mimeType ?: "*/*"), this)
}

fun String.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), this)
}