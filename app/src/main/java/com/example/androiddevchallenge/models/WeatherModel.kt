/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.models

import com.example.androiddevchallenge.R
val iconsDrawable = arrayListOf(
    R.drawable.windy_cloudy_night,
    R.drawable.sunny_foggy_color,
    R.drawable.thunder
)
val degrees = arrayListOf(
    "21°", "15°", "12°", "31°"
)

data class WeatherModel(
    val city: String,
    val icon: Int,
    val iconDesc: String,
    val degree: String
) {
    val otherDays = arrayListOf(
        OtherDayModel(iconsDrawable.random(), degrees.random(), "Monday", "17 August"),
        OtherDayModel(iconsDrawable.random(), degrees.random(), "Tuesday", "18 August"),
        OtherDayModel(iconsDrawable.random(), degrees.random(), "Wednesday", "17 August")
    )
}

data class OtherDayModel(
    val icon: Int,
    val degree: String,
    val day: String,
    val date: String
)
