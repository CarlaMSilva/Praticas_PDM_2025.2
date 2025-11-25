package com.example.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.ui.HomePage
import com.example.weatherapp.ui.ListPage
import com.example.weatherapp.ui.MapPage

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Route.Home) {
        composable<BottomNavItem.Route.Home> {  HomePage(modifier = Modifier, viewModel = viewModel()) }
        composable<BottomNavItem.Route.List> { ListPage(viewModel = viewModel()) }
        composable<BottomNavItem.Route.Map> { MapPage(viewModel = viewModel()) }
    }
}
