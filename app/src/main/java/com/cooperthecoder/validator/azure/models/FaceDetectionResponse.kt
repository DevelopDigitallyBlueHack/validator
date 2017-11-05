package com.cooperthecoder.validator.azure.models

data class FaceDetectionResponse(
        val faceId: String,
        val faceRectangle: FaceRectangle
)