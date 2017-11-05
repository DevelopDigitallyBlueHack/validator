package com.cooperthecoder.validator.azure.models

import com.squareup.moshi.Json

class Celebrity {

    @Json(name = "name")
    var name: String? = null
    @Json(name = "faceRectangle")
    var faceRectangle: FaceRectangle? = null
    @Json(name = "confidence")
    var confidence: Double? = null

}