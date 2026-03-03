package com.example.weatherapp.viewmodel

import androidx.browser.browseractions.BrowserServiceFileProvider.loadBitmap
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.api.toForecast
import com.example.weatherapp.api.toWeather
import com.example.weatherapp.db.fb.FBCity
import com.example.weatherapp.db.fb.FBDatabase
import com.example.weatherapp.db.fb.FBUser
import com.example.weatherapp.db.fb.toFBCity
import com.example.weatherapp.model.City
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.User
import com.example.weatherapp.model.Weather
import com.example.weatherapp.ui.nav.BottomNavItem
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val db: FBDatabase,
    private val service: WeatherService
) : ViewModel(), FBDatabase.Listener {


    private val _cities = mutableStateMapOf<String, City>()

    val cities: List<City>
        get() = _cities.values.toList().sortedBy { it.name }

    private val _forecast = mutableStateMapOf<String, List<Forecast>?>()

    private val _weather = mutableStateMapOf<String, Weather>()
    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    init {
        db.setListener(this)
    }

    private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            apiWeather?.let {

                _weather[name] = it.toWeather()
                loadBitmap(name)
            }
        }
    }

    fun loadBitmap(name: String) {
        _weather[name]?.let { weather ->
            service.getBitmap(weather.imgUrl) { bitmap ->
                _weather[name] = weather.copy(bitmap = bitmap)
            }
        }
    }

    private var _city = mutableStateOf<String?>(null)
    var city: String?
        get() = _city.value
        set (tmp) { _city.value = tmp
        }
    private fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->
            apiForecast?.let {
                _forecast[name] = apiForecast.toForecast()
            }
        }
    }

    private var _page = mutableStateOf<BottomNavItem.Route>(BottomNavItem.Route.Home)
    var page: BottomNavItem.Route
        get() = _page.value
        set (tmp) { _page.value = tmp }

    fun forecast(name: String) = _forecast.getOrPut(name) {
        loadForecast(name)
        emptyList ()
    }

    fun weather(name: String) = _weather.getOrPut(name) {
        loadWeather(name)
        Weather.LOADING
    }

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

    fun add(name: String, location: LatLng? = null) {
        db.add(City(name = name, location = location).toFBCity())
    }

    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name = name, location = LatLng(lat, lng)).toFBCity())
            }
        }
    }

    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(City(name = name, location = location).toFBCity())
            }
        }
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        _user.value = null
        _cities.clear()
        _weather.clear()
    }


    override fun onCityAdded(city: FBCity) {
        val cityName = city.name ?: return

        _cities[cityName] = city.toCity()
    }

    override fun onCityUpdated(city: FBCity) {
        val cityName = city.name ?: return
        _cities[cityName] = city.toCity()
    }

    override fun onCityRemoved(city: FBCity) {

        _cities.remove(city.name)
    }
}