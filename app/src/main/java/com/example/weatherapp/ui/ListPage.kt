package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.camera.core.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.City
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ExitToApp
//import androidx.compose.runtime.saveable.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.model.Weather
import com.example.weatherapp.ui.nav.BottomNavItem
import com.example.weatherapp.viewmodel.MainViewModel




//@Preview(showBackground = true)

@Composable
fun ListPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val cityList = viewModel.cities
    val activity = LocalActivity.current as? Activity

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            ) {
        items(items = cityList, key = { it.name }) { city ->
            CityItem(
                city = city, weather = viewModel.weather(city.name),
                onClick = {
                    viewModel.city = city.name
                    viewModel.page = BottomNavItem.Route.Home
                    Toast.makeText(
                        activity, "Cidade Favoritada",
                        Toast.LENGTH_LONG
                    ).show()
                },
                        onClose = {
                            viewModel.remove(city)
                            Toast.makeText(
                                activity, "Cidade Deletada!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }


            }
}

    @Composable
    fun CityItem(
        city: City,
        weather: Weather,
        onClick: () -> Unit,
        onClose: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val desc = if (weather == Weather.LOADING) "Carregando clima..." else weather.desc
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                // Substitui o Icon(...)
                model = weather.imgUrl,
                modifier = Modifier.size(75.dp),
                error = painterResource(id = R.drawable.loading),
                contentDescription = "Imagem" )
            Icon(
                Icons.Rounded.FavoriteBorder,
                contentDescription = ""
            )
            Spacer (modifier = Modifier.size(12.dp))
            Column (modifier = modifier.weight(1f)) {
                Text(
                    modifier = Modifier,
                    text = city.name,
                    fontSize = 24.sp
                )
                Text (modifier = Modifier,
//                    text = city.weather ?: "Carregando clima...",
                    text = desc,
                    fontSize = 16.sp
                )
            }
            IconButton (onClick = onClose) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        }
    }

