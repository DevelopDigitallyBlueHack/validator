package com.cooperthecoder.validator.azure.models

data class FaceVerificationRequest(
        val faceId1: String,
        val faceId2: String
)