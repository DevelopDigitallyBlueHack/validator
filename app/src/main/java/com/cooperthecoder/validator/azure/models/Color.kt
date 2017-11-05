package com.cooperthecoder.validator.azure.models

data class Color(
        val dominantColorForeground: String,
        val dominantColorBackground: String,
        val dominantColors: List<String>,
        val accentColor: String,
        var isBWImg: Boolean
)
