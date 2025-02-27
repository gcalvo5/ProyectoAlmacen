package com.example.proyectoalmacen.view

import CustomTextView
import TextViewConfig
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectoalmacen.ProyectoAlmacenAplication.Companion.preferens
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.core.navigation.NavigationWrapper
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.HojaCarga
import com.example.proyectoalmacen.model.DataClasses.Plazas
import com.example.proyectoalmacen.model.States.EstadilloState
import com.example.proyectoalmacen.model.States.ExpedicionState
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.ui.theme.ProyectoAlmacenTheme
import com.example.proyectoalmacen.view.carga.CreateCargaScreen
import com.example.proyectoalmacen.view.commons.basicComponents.CustomComparationText
import com.example.proyectoalmacen.view.commons.basicComponents.CustomIconButton
import com.example.proyectoalmacen.view.commons.basicComponents.CustomLoader
import com.example.proyectoalmacen.view.commons.basicComponents.CustomTopBar
import com.example.proyectoalmacen.view.estadillo.CreateEstadilloScreen
import com.example.proyectoalmacen.viewmodel.BultoViewModel
import com.example.proyectoalmacen.viewmodel.EstadilloViewModel
import com.example.proyectoalmacen.viewmodel.ExpedicionViewModel
import com.example.proyectoalmacen.viewmodel.ExpedicionesQueryType
import com.example.proyectoalmacen.viewmodel.HojaCargaViewModel
import com.example.proyectoalmacen.viewmodel.PlazasViewModel
import com.example.proyectoalmacen.viewmodel.UsuarioViewModel
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Load initial configuracions
        //TO DO Load user and confugure based on that
        preferens.saveName()
        preferens.saveUserId()

        //Configuracion de Crashalytics
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
        Firebase.crashlytics.log("Crashlytics enabled")
        Firebase.crashlytics.setUserId(preferens.getUserId())
        Firebase.crashlytics.setCustomKey("user_name", preferens.getName())


        setContent {
            ProyectoAlmacenTheme {
                Firebase.crashlytics.log("HomeActivityEntered")
                NavigationWrapper()
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)


data class ItemData(val id: Int, val name: String, val description: String)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, bultoViewModel: BultoViewModel = hiltViewModel(),expedicionViewModel: ExpedicionViewModel = hiltViewModel(), estadilloViewModel: EstadilloViewModel = hiltViewModel(), navigateToEstadillo: (numEstadillo: Int, nombreChofer: String) -> Unit = { numEstadillo: Int, nombreChofer: String -> }, navigateToRepaso: (numPlaza: Int) -> Unit = { numPlaza: Int -> }, navigateToCarga: (plazas: List<Int>) -> Unit = { plazas: List<Int> -> }) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    // Use LaunchedEffect to read the saved state only once when the composable is first launched
    LaunchedEffect(navBackStackEntry) {
        val savedTabIndex = navBackStackEntry?.savedStateHandle?.get<Int>("tabIndex")
        if (savedTabIndex != null) {
            selectedTabIndex = savedTabIndex
        }
    }
    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            title = stringResource(R.string.resumen_text),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = stringResource(R.string.estadillos_text),
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        ),
        BottomNavigationItem(
            title = stringResource(R.string.repaso_text),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        ),
        BottomNavigationItem(
            title = stringResource(R.string.cargas_text),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
                modifier = Modifier.imePadding(),
        topBar = { CustomTopBar(navController = navController ,title = bottomNavigationItems[selectedTabIndex].title, showConfigButton = true, showHomeButton = false) },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.wrapContentHeight(),
                containerColor = colorScheme.background
            ) {
                bottomNavigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index
                            navBackStackEntry?.savedStateHandle?.set("tabIndex", index)},
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (selectedTabIndex == index) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    modifier = Modifier.padding(bottom = 0.dp),
                                    tint = if(selectedTabIndex == index) colorScheme.secondary else colorScheme.tertiary

                                )
                                CustomTextView(
                                    type = TextViewType.SINGLE,
                                    mainText = item.title,
                                    config = TextViewConfig( mainTextFontSize = 16.sp,mainTextColor = if(selectedTabIndex == index) colorScheme.secondary else colorScheme.tertiary)
                                )
                            }
                        },
                        label = null
                    )
                }
            }
        },
    ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                when (selectedTabIndex) {
                    0 -> Tab1Content()
                    1 -> Tab2Content(navigateToEstadillo = navigateToEstadillo)
                    2 -> Tab3Content(navigateToRepaso)
                    3 -> Tab4Content(navigateToCarga = navigateToCarga)
                }
            }

    }
}

@Composable
fun Tab1Content(estadilloViewModel: EstadilloViewModel = hiltViewModel()) {
    val uiStateEstadillo by estadilloViewModel.estadillosList.collectAsState()
    var loadingEstadillo by remember { mutableStateOf(false) }
    val estadillos by remember {
        derivedStateOf {
            (uiStateEstadillo as? UiState.Success<List<Estadillo>>)?.data ?: emptyList()
        }
    }
    when (uiStateEstadillo) {
        is UiState.Loading -> {
            loadingEstadillo = true
            CustomLoader(loadingEstadillo)
        }
        is UiState.Success -> {
            Log.i("Bultos Success", "${(uiStateEstadillo as UiState.Success<*>).data}")
            loadingEstadillo = false
        }
        is UiState.Error -> Log.e("Bultos Error,", (uiStateEstadillo as UiState.Error).message)
    }
    // Use a Box to create an overlay effect
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content (always visible)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextView(type = TextViewType.TITLE_AND_SUBTITLE, "Resumen")
            Card(
                modifier = Modifier
                    .padding(vertical = 30.dp)
                    .fillMaxWidth(),
                colors = CardColors(
                    containerColor = colorScheme.primaryContainer,
                    contentColor = colorScheme.primary,
                    disabledContainerColor = colorScheme.secondaryContainer,
                    disabledContentColor = colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(estadillos) { item ->
                            ItemRow(item = item)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Contenido de la pantalla")
            Spacer(modifier = Modifier.height(16.dp))
            Text("More content")
        }
    }
}

@Composable
fun ItemRow(item: Estadillo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            CustomTextView(type = TextViewType.SINGLE, mainText = item.numEst.toString())
            CustomTextView(type = TextViewType.SINGLE, mainText = item.conductor.nombre)
            HorizontalDivider()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Tab2Content(estadilloViewModel: EstadilloViewModel = hiltViewModel(), navigateToEstadillo: (numEstadillo: Int, nombreChofer: String) -> Unit = { numEstadillo: Int, nombreChofer: String -> }) {
    val uiStateEstadillo by estadilloViewModel.estadillosList.collectAsState()
    var loadingEstadillo by remember { mutableStateOf(false) }
    val estadillos by remember {
        derivedStateOf {
            (uiStateEstadillo as? UiState.Success<List<Estadillo>>)?.data ?: emptyList()
        }
    }
    when (uiStateEstadillo) {
        is UiState.Loading -> {
            loadingEstadillo = true
            CustomLoader(loadingEstadillo)
        }
        is UiState.Success ->{
            loadingEstadillo = false
            Log.i("Estadillo Success", "${(uiStateEstadillo as UiState.Success<*>).data}")
        }
        is UiState.Error -> Log.e("Estadillo Error,", (uiStateEstadillo as UiState.Error).message)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var createEstadilloDialogisVisible by remember { mutableStateOf(false) }
        CustomIconButton(text = stringResource(R.string.crear_text), icon = Icons.Filled.Add, onClick = { createEstadilloDialogisVisible = !createEstadilloDialogisVisible })
        if (createEstadilloDialogisVisible) {
            CreateEstadilloScreen(onDismissRequest = {createEstadilloDialogisVisible = false}, navigateToEstadillo = navigateToEstadillo)
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
        ) {
            items(estadillos) { item ->
                ItemRowListaEstadillosGeneral(item = item, navigateToEstadillo)
                }
            }
        }
}

    @Composable
    fun ItemRowListaEstadillosGeneral(item: Estadillo, navigateToEstadillo: (numEstadillo: Int, nombreChofer: String) -> Unit = { numEstadillo: Int, nombreChofer: String -> }) {
        var colorPrimary = colorScheme.primary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(90.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
                Card (modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.primaryContainer)
                    .clickable { navigateToEstadillo(item.numEst, item.conductor.nombre) }){
                    Row (modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth()
                        .height(29.dp), horizontalArrangement = Arrangement.Center) {
                        CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.numero_Estadillo_text), config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.tertiary))
                        Spacer(modifier = Modifier.width(5.dp))
                        CustomTextView(type = TextViewType.SINGLE, mainText = item.numEst.toString(), config = TextViewConfig(mainTextFontSize = 18.sp))
                        Spacer(modifier = Modifier.width(15.dp))
                        CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.autor_text), config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.tertiary))
                        Spacer(modifier = Modifier.width(5.dp))
                        CustomTextView(type = TextViewType.SINGLE, mainText = item.usuario.nombre, config = TextViewConfig(mainTextFontSize = 18.sp))
                    }
                    Row (modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth()
                        .height(29.dp), horizontalArrangement = Arrangement.Center) {
                        CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.hora_text), config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.tertiary))
                        Spacer(modifier = Modifier.width(5.dp))
                        CustomTextView(type = TextViewType.SINGLE, mainText = item.tiempoCreacion, config = TextViewConfig(mainTextFontSize = 18.sp))
                        Spacer(modifier = Modifier.width(15.dp))
                        CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.conductor_text), config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.tertiary))
                        Spacer(modifier = Modifier.width(5.dp))
                        CustomTextView(type = TextViewType.SINGLE, mainText = item.conductor.nombre, config = TextViewConfig(mainTextFontSize = 18.sp))
                    }
                    Row (modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth()
                        .height(29.dp), horizontalArrangement = Arrangement.Center) {
                        CustomTextView(type = TextViewType.SINGLE, mainText = (item.numBultosConfirmados + item.numBultosRepasados + item.numBultosCargados).toString(), config = TextViewConfig(mainTextFontSize = 18.sp))
                        Canvas(modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(10.dp, 20.dp)) {
                            drawLine(
                                color = colorPrimary,
                                start = Offset(3f, size.height),
                                end = Offset(size.width, 3f),
                                strokeWidth = 2f
                            )
                        }
                        CustomTextView(type = TextViewType.SINGLE, mainText = item.numBultosTotal.toString(), config = TextViewConfig(mainTextFontSize = 18.sp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(imageVector = Icons.Filled.Place, contentDescription = "Llegadas", tint = colorPrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        CustomTextView(type = TextViewType.SINGLE, mainText = (item.numBultosRepasados + item.numBultosCargados).toString(), config = TextViewConfig(mainTextFontSize = 18.sp))
                        Canvas(modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(10.dp, 20.dp)) {
                            drawLine(
                                color = colorPrimary,
                                start = Offset(3f, size.height),
                                end = Offset(size.width, 3f),
                                strokeWidth = 2f
                            )
                        }
                        CustomTextView(type = TextViewType.SINGLE, mainText = item.numBultosTotal.toString(), config = TextViewConfig(mainTextFontSize = 18.sp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Repasados", tint = colorPrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        CustomTextView(type = TextViewType.SINGLE, mainText = item.numBultosCargados.toString(), config = TextViewConfig(mainTextFontSize = 18.sp))
                        Canvas(modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(10.dp, 20.dp)) {
                            drawLine(
                                color = colorPrimary,
                                start = Offset(3f, size.height),
                                end = Offset(size.width, 3f),
                                strokeWidth = 2f
                            )
                        }
                        CustomTextView(type = TextViewType.SINGLE, mainText = item.numBultosTotal.toString(), config = TextViewConfig(mainTextFontSize = 18.sp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Partidas", tint = colorPrimary)

                    }
                }

        }
    }

    @Composable
    fun Tab3Content(navigateToRepaso: (numPlaza: Int) -> Unit, expedicionViewModel: ExpedicionViewModel = hiltViewModel()) {
        val uiStateExpedicion by expedicionViewModel.expedicionList.collectAsState()
        var loadingExpedicion by remember { mutableStateOf(false) }
        val expediciones by remember {
            derivedStateOf {
                (uiStateExpedicion as? UiState.Success<List<Expedicion>>)?.data ?: emptyList()
            }
        }
        when (uiStateExpedicion) {
            is UiState.Loading -> {
                loadingExpedicion = true
                CustomLoader(loadingExpedicion)
            }
            is UiState.Success -> {
                loadingExpedicion = false
                Log.i("Expediciones Success", "${(uiStateExpedicion as UiState.Success<*>).data}")
            }
            is UiState.Error -> Log.e("Expediciones Error,", (uiStateExpedicion as UiState.Error).message)
        }
        var plazasViewModel: PlazasViewModel = hiltViewModel()
        val plazasState = plazasViewModel.plazasState
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var bultosTotales = 0
            var bultosRepasadosTotales = 0
            if (expedicionViewModel.getListNumBultosTotales().isNotEmpty()) {
                bultosTotales = expedicionViewModel.getListNumBultosTotales()
                    .get(1) + expedicionViewModel.getListNumBultosTotales().get(2)
                bultosRepasadosTotales = expedicionViewModel.getListNumBultosTotales().get(2)
            }
            CustomTextView(type = TextViewType.TITLE_AND_SUBTITLE, stringResource(R.string.repaso_por_plazas_text), subtitle = stringResource(R.string.total_repasados_Text, bultosRepasadosTotales, bultosTotales))
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
            ) {
                items(plazasState.plazas) { item ->
                    ItemRowListaPlazasRepasar(item = item, navigateToRepaso)
                }
            }
        }
    }
@Composable
fun ItemRowListaPlazasRepasar(item: Plazas, navigateToRepaso: (numPlaza: Int) -> Unit = { numPlaza: Int -> }){
    var expedicionViewModel: ExpedicionViewModel = hiltViewModel()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card (modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.primaryContainer)
            .align(Alignment.CenterVertically)
            .clickable { navigateToRepaso(item.codPlazas) }) {
            Row(
                modifier = Modifier
                    .padding(1.dp)
                    .fillMaxWidth()
                    .fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                val coroutineScope = rememberCoroutineScope()
                CustomTextView(type = TextViewType.SINGLE, mainText = item.nombrePlaza, modifier = Modifier.width(130.dp))
                Spacer(modifier = Modifier.width(20.dp))
                var bultosRepasadosPlaza by remember { mutableStateOf(0) }
                var bultosConfirmadosPlaza by remember { mutableStateOf(0) }
                if (expedicionViewModel.getListNumBultosTotales().isNotEmpty()) {
                    bultosRepasadosPlaza =
                        expedicionViewModel.getListNumBultosTotales(item.codPlazas).get(2)
                    bultosConfirmadosPlaza =
                        expedicionViewModel.getListNumBultosTotales(item.codPlazas).get(1)
                }
                CustomComparationText(
                    firstText = bultosRepasadosPlaza
                        .toString(),
                    secondText = (bultosConfirmadosPlaza + bultosRepasadosPlaza).toString(),
                    modifier = Modifier.width(80.dp),
                    icon = Icons.Filled.Check
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Tab4Content(hojasCargaViewModel: HojaCargaViewModel = hiltViewModel(), navigateToCarga: (plazas: List<Int>) -> Unit = { plazas: List<Int> -> }) {
    val uiStateHojasCarga by hojasCargaViewModel.hojasCargaList.collectAsState()
    var loadingHojasCarga by remember { mutableStateOf(false) }
    val hojasCarga by remember {
        derivedStateOf {
            (uiStateHojasCarga as? UiState.Success<List<HojaCarga>>)?.data ?: emptyList()
        }
    }
    when (uiStateHojasCarga) {
        is UiState.Loading -> {
            loadingHojasCarga = true
            CustomLoader(loadingHojasCarga)
        }
        is UiState.Success -> {
            loadingHojasCarga = false
            Log.i("Expediciones Success", "${(uiStateHojasCarga as UiState.Success<*>).data}")
        }
        is UiState.Error -> Log.e("Expediciones Error,", (uiStateHojasCarga as UiState.Error).message)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var createHojaCargaDialogisVisible by remember { mutableStateOf(false) }
        CustomIconButton(text = stringResource(R.string.crear_text), icon = Icons.Filled.Add, onClick = { createHojaCargaDialogisVisible = !createHojaCargaDialogisVisible })
        if (createHojaCargaDialogisVisible) {
            CreateCargaScreen(onDismissRequest = {}, navigateToCarga = navigateToCarga)
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
        ) {
            items(hojasCarga) { item ->
                ItemRowListaHojasCarga(item = item, navigateToCarga)
            }
        }
    }
}

@Composable
fun ItemRowListaHojasCarga(item: HojaCarga, navigateToCarga: (plazas: List<Int>) -> Unit = { plazas: List<Int> -> }){
    val usuarioViewModel: UsuarioViewModel = hiltViewModel()
    val expedicionViewModel: ExpedicionViewModel = hiltViewModel()
    val plazasViewModel: PlazasViewModel = hiltViewModel()
    var bultosTotales = 0
    var bultosCargados = 0
    var plazas = ""
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(90.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card (modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.primaryContainer)
            .clickable { navigateToCarga(item.idPlazas) }){
            Row (modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth()
                .height(29.dp), horizontalArrangement = Arrangement.Center) {
                CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.numero_hoja_carga), config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.tertiary))
                Spacer(modifier = Modifier.width(5.dp))
                CustomTextView(type = TextViewType.SINGLE, mainText = item.numHojaCarga.toString(), config = TextViewConfig(mainTextFontSize = 18.sp))
                Spacer(modifier = Modifier.width(15.dp))
                CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.autor_text), config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.tertiary))
                Spacer(modifier = Modifier.width(5.dp))
                CustomTextView(type = TextViewType.SINGLE, mainText = usuarioViewModel.getUsuarioById(item.idUsuario).nombre, config = TextViewConfig(mainTextFontSize = 18.sp))
            }
            Row (modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth()
                .height(29.dp), horizontalArrangement = Arrangement.Center) {
                CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.hora_text), config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.tertiary))
                Spacer(modifier = Modifier.width(5.dp))
                CustomTextView(type = TextViewType.SINGLE, mainText = item.tiempoCreacion, config = TextViewConfig(mainTextFontSize = 18.sp))
                Spacer(modifier = Modifier.width(15.dp))
                if (expedicionViewModel.getListNumBultosTotales().isNotEmpty()) {
                    item.idPlazas.forEach({
                        bultosTotales += expedicionViewModel.getListNumBultosTotales(it).get(0)
                        bultosCargados += expedicionViewModel.getListNumBultosTotales(it).get(3)
                        plazas += plazasViewModel.getPlazaById(it).nombrePlaza + ", "
                    })
                    plazas = plazas.dropLast(2)
                }

                CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.bultos_cargados_text), config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.tertiary))
                Spacer(modifier = Modifier.width(5.dp))
                CustomComparationText(firstText = bultosCargados.toString(), secondText = bultosTotales.toString())
            }
            Row (modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth()
                .height(29.dp), horizontalArrangement = Arrangement.Center) {
                CustomTextView(type = TextViewType.SINGLE, mainText = stringResource(R.string.plazas_text), config = TextViewConfig(mainTextFontSize = 18.sp))
                Spacer(modifier = Modifier.width(5.dp))
                CustomTextView(type = TextViewType.SINGLE, mainText = plazas, config = TextViewConfig(mainTextFontSize = 18.sp))
            }
        }

    }
}