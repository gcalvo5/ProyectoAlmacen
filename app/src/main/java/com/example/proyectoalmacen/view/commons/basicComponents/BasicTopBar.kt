package com.example.proyectoalmacen.view.commons.basicComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectoalmacen.core.navigation.Home
import com.example.proyectoalmacen.ui.theme.ProyectoAlmacenTheme
import com.example.proyectoalmacen.ui.theme.Transparent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    showHomeButton: Boolean = true,
    showConfigButton: Boolean = false,
    settingsIcon: ImageVector = Icons.Filled.Settings,
    progress: Pair<Int, Int>? = null,
    navController: NavController
) {
    TopAppBar(
        title = {
            Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){

                Text(text = title, textAlign = TextAlign.Center)
                if (progress != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${progress.first}/${progress.second}")
                }
            }
        },
        navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = colorScheme.secondary)
                }
        },
        actions = {
            if (showConfigButton) {
                IconButton(onClick = { /* Handle custom icon click */ }) {
                    Icon(settingsIcon, contentDescription = "Config", tint = colorScheme.secondary)
                }
            }else if (showHomeButton) {
                IconButton(onClick = { navController.navigate(Home) }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home", tint = colorScheme.secondary)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Transparent,
            titleContentColor = colorScheme.secondary
        )
    )
}