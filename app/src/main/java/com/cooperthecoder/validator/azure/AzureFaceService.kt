package com.cooperthecoder.validator.azure

import com.cooperthecoder.validator.azure.models.FaceDetectionResponse
import com.cooperthecoder.validator.azure.models.FaceVerificationRequest
import com.cooperthecoder.validator.azure.models.FaceVerificationResponse
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface AzureFaceService {
    @POST("/face/v1.0/detect")
    fun detectFace(@Body facePhoto: RequestBody): Single<List<FaceDetectionResponse>>

    @POST("/face/v1.0/verify")
    fun verifyFace(@Body request: FaceVerificationRequest): Single<FaceVerificationResponse>
}