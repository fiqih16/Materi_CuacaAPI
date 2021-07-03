package com.plete.newyorkweather

import com.squareup.moshi.Json

data class OpenWeatherMapResponseData(
        @field:Json(name = "name")
        val locationName: String,
        val weather: List<OpenWeatherMapWeatherData>
    )
