package com.cooperthecoder.validator.azure

import android.graphics.Bitmap
import android.util.Log
import com.cooperthecoder.validator.azure.models.FaceDetectionResponse
import com.cooperthecoder.validator.azure.models.FaceVerificationRequest
import com.cooperthecoder.validator.azure.models.FaceVerificationResponse
import com.cooperthecoder.validator.toByteArray
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class AzureFaceClient(apiKey: String) {
    private val azureFaceService: AzureFaceService by lazy {
        val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                            .addHeader("Ocp-Apim-Subscription-Key", apiKey)
                            .build()
                    chain.proceed(request)
                }
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://eastus.api.cognitive.microsoft.com")
                .client(client)
                .build()
        retrofit.create(AzureFaceService::class.java)
    }

    fun verifyFace(request: FaceVerificationRequest): Single<FaceVerificationResponse> {
        return azureFaceService.verifyFace(request)
    }

    fun detectFace(bitmap: Bitmap): Single<List<FaceDetectionResponse>> {
        val bitmapBytes = bitmap.toByteArray()
        val facePhoto = RequestBody.create(MediaType.parse("application/octet-stream"), bitmapBytes)
        Log.d(javaClass.name, "Sending ${bitmapBytes.size} bytes to Face API")
        return azureFaceService.detectFace(facePhoto)
    }
}