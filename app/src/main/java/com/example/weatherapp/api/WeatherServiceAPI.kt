package com.example.weatherapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceAPI {
    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = "e0981555c50b4f9bb2d225844260501"
    }

    @GET("current.json?key=$API_KEY&lang=pt")
    fun weather(@Query("q") query: String): Call<APICurrentWeather?>

    @GET("search.json")
    fun search(
        @Query("key") apiKey: String = API_KEY,
        @Query("q") query: String,
    ): Call<List<APILocation>>

    @GET("forecast.json?key=$API_KEY&days=10&lang=pt")
    fun forecast(@Query("q") query: String): Call<APIWeatherForecast?>
}
