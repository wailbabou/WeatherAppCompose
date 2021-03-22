package com.example.androiddevchallenge.models

import com.example.androiddevchallenge.R
val iconsDrawable = arrayListOf(
    R.drawable.windy_cloudy_night,
    R.drawable.sunny_foggy_color,
    R.drawable.thunder
)
val degrees = arrayListOf(
    "21°","15°","12°","31°"
)

data class WeatherModel (
    val city:String,
    val icon:Int,
    val iconDesc : String,
    val degree:String){
    val otherDays = arrayListOf(
        OtherDayModel( iconsDrawable.random(),degrees.random(),"Monday","17 August"),
        OtherDayModel( iconsDrawable.random(),degrees.random(),"Tuesday","18 August"),
        OtherDayModel( iconsDrawable.random(), degrees.random(),"Wednesday","17 August")
    )
}

data class OtherDayModel(
    val icon:Int,
    val degree:String,
    val day : String,
    val date : String
    )