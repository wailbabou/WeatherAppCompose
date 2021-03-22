package com.example.androiddevchallenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.models.WeatherModel

class MainViewModel : ViewModel() {
    val items = arrayListOf(
        WeatherModel("Setif", R.drawable.sunny_foggy_color,"Sunny Day Icon","21°"),
        WeatherModel("Algiers", R.drawable.windy_cloudy_night,"Windy Cloudy Night","10°"),
        WeatherModel("Constantine", R.drawable.thunder,"Thunder Day","7°")
    )

    private val _favoriteCity = MutableLiveData(items[0])
    val favoriteCity = _favoriteCity

    private val _selectedCity = MutableLiveData<WeatherModel>(items[0])
    val selectedCity : LiveData<WeatherModel> = _selectedCity

    fun setSelectedCity(item: WeatherModel){
        _selectedCity.value = item
    }
}