package com.cooperthecoder.validator.azure.models

data class OCR(
        val language: String,
        val textAngle: Double,
        val orientation: String,
        val regions: List<Region>
)