package com.cooperthecoder.validator.azure

import com.cooperthecoder.validator.azure.models.AnalysisResult
import com.cooperthecoder.validator.azure.models.OCR
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AzureVisionService {
    @POST("/vision/v1.0/analyze")
    fun analyze(
            @Body photo: RequestBody,
            @Query("visualFeatures") visualFeatures: String,
            @Query("details") details: String,
            @Query("language") language: String
    ): Single<AnalysisResult>

    @POST("/vision/v1.0/ocr")
    fun readText(
            @Body photo: RequestBody,
            @Query("language") language: String,
            @Query("detectOrientation") detectOrientation: Boolean
    ): Single<OCR>

}