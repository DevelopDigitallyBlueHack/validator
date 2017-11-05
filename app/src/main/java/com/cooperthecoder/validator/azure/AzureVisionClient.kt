package com.cooperthecoder.validator.azure

import android.graphics.Bitmap
import android.util.Log
import com.cooperthecoder.validator.azure.models.AnalysisResult
import com.cooperthecoder.validator.azure.models.OCR
import com.cooperthecoder.validator.toByteArray
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class AzureVisionClient(apiKey: String) {
    private val azureVisionService: AzureVisionService by lazy {
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
        retrofit.create(AzureVisionService::class.java)
    }

    fun read(bitmap: Bitmap): Single<OCR> {
        val bitmapBytes = bitmap.toByteArray()
        Log.d(javaClass.name, "Sending ${bitmapBytes.size} bytes to Vision API")
        val requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), bitmapBytes)
        return azureVisionService.readText(requestBody, "en", true)
    }

    fun analyze(bitmap: Bitmap): Single<AnalysisResult> {
        val bitmapBytes = bitmap.toByteArray()
        Log.d(javaClass.name, "Sending ${bitmapBytes.size} bytes to Analysis API")
        val emptyString = ""
        val requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), bitmapBytes)
        return azureVisionService.analyze(requestBody, emptyString, emptyString, "en")
    }
}