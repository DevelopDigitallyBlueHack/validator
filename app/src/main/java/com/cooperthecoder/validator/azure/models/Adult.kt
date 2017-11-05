package com.cooperthecoder.validator.azure.models

data class Adult(
        val isAdultContent: Boolean,
        val isRacyContent: Boolean,
        val adultScore: Double,
        val racyScore: Double
)