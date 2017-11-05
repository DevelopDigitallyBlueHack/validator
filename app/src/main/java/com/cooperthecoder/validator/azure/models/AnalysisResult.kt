package com.cooperthecoder.validator.azure.models

data class AnalysisResult(
    val categories: List<Category>,
    val adult: Adult,
    val tags: List<Tag>?,
    val description: Description,
    val requestId: String,
    val metadata: Metadata,
    val faces: List<Face>,
    val color: Color,
    val imageType: ImageType
)