package com.cooperthecoder.validator.azure.models

data class FaceVerificationResponse(
        val isIdentical: Boolean,
        val confidence: Float
)