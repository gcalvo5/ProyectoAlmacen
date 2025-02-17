package com.example.proyectoalmacen.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectoalmacen.view.HomeScreen
import com.example.proyectoalmacen.view.carga.CargaScreen
import com.example.proyectoalmacen.view.estadillo.EstadilloScreen
import com.example.proyectoalmacen.view.repaso.RepasoScreen
import com.example.proyectoalmacen.viewmodel.EstadilloViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(estadilloViewModel: EstadilloViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(navController = navController, estadilloViewModel,
            navigateToEstadillo = {
                numEstadillo: Int, nombreChofer: String ->
                navController.navigate(
                    Estadillo(numEstadillo = numEstadillo, nombreChofer = nombreChofer))
            },
            navigateToRepaso = {navController.navigate(Repaso(numPlaza = it))},
            navigateToCarga = {navController.navigate(Carga(plazas = it))}
            )
        }

        composable<Estadillo> { backStackEntry ->
            val  estadillo:Estadillo = backStackEntry.toRoute<Estadillo>()
            EstadilloScreen(navController = navController, estadillo.numEstadillo, estadillo.nombreChofer)
        }

        composable<Repaso> { backStackEntry ->
            val  repaso:Repaso = backStackEntry.toRoute<Repaso>()
            RepasoScreen(navController = navController, codPlaza = repaso.numPlaza)
        }

        composable<Carga> { backStackEntry ->
            val  carga:Carga = backStackEntry.toRoute<Carga>()
            CargaScreen(navController = navController, plazas = carga.plazas)
        }


    }
}