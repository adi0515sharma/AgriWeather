package com.example.agriweather.android

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.agriweather.Models.WeatherModels.CurrentWeatherResponse
import com.example.agriweather.Repository.checkLocationPermission
import com.example.agriweather.ViewModel.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (fineLocationGranted || coarseLocationGranted) {
                lifecycleScope.launch {
                    viewModel.fetchCurrentWeather()
                }
            }
        }

        setContent {
            MyApplicationTheme {
                val weatherData by viewModel.uiState.collectAsStateWithLifecycle()
                val loading by viewModel.isLoading.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                var showLocationAlert by rememberSaveable { mutableStateOf<Boolean>(false) }
                val localCoroutine = rememberCoroutineScope()




                val lifecycleOwner = LocalLifecycleOwner.current

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            localCoroutine.launch {
                                viewModel.fetchCurrentWeather()
                            }
                        }
                    }

                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                LaunchedEffect(Unit) {
                    viewModel.errorMessage.collect { message ->


                        message?.let {
                            snackbarHostState.showSnackbar(message)

                        }
                    }
                }
                LaunchedEffect(Unit) {



                    viewModel.askLocationPermission.collectLatest {
                        if(it == false){
                            return@collectLatest
                        }

                        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
                        val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION



                        if (shouldShowRequestPermissionRationale(fineLocationPermission) ||
                            shouldShowRequestPermissionRationale(coarseLocationPermission)) {

                            showLocationAlert = true

                        } else {
                            requestPermissionsLauncher.launch(
                                arrayOf(fineLocationPermission, coarseLocationPermission)
                            )
                        }
                        viewModel.changeLocationAsk(false)
                    }

                }


                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { padding ->
                    Surface(
                        modifier = Modifier.padding(padding).fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,

                        ) {

                        if (loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(enabled = false) {}
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            return@Surface
                        }
                        LocationPermissionDialog(showLocationAlert) {
                            showLocationAlert = false
                        }

                        weatherData?.let {
                            WeatherCard(weatherData!!)
                        }

                    }
                }
            }
        }
    }


}




@Composable
fun LocationPermissionDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    openAppSettings(context)
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            },
            title = { Text("Permission Required") },
            text = {
                Text("Location permission is required for this feature. Please enable it from app settings.")
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}



@Composable
fun WeatherCard(weather: CurrentWeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "📍 ${weather.latitude}, ${weather.longitude}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val isDay = weather.current_weather.is_day.toInt() == 1
        Text(
            text = if (isDay) "☀️" else "🌙",
            fontSize = 200.sp
        )

        Text(
            text = "🕒 ${weather.current_weather.time}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE3F2FD))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherStat("🌡️", "Temp", "${weather.current_weather.temperature} ${weather.current_weather_units.temperature}")
                WeatherStat("💨", "Wind", "${weather.current_weather.windspeed?.toInt()} ${weather.current_weather_units.windspeed}")
                WeatherStat("🧭", "Dir", "${weather.current_weather.winddirection?.toInt()}°")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherStat("📍", "Elev", "${weather.elevation} m")
                WeatherStat("⏱️", "Interval", "${weather.current_weather.interval.toInt()} ${weather.current_weather_units.interval}")
            }
        }
    }
}


@Composable
fun WeatherStat(icon: String, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Text(text = icon, fontSize = 24.sp)
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value, fontSize = 14.sp, color = Color.DarkGray)
    }
}
@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
