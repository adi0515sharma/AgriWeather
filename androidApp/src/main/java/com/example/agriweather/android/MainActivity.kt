package com.example.agriweather.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agriweather.Models.CurrentWeatherResponse
import com.example.agriweather.ViewModel.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val viewModel: MainActivityViewModel = viewModel()
                val weatherData by viewModel.uiState.collectAsStateWithLifecycle()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    weatherData?.let {
                        WeatherCard(weatherData!!)
                    }

                }
            }
        }
    }
}

@Composable
fun WeatherCard(weather: CurrentWeatherResponse) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🌍 Location: ${weather.latitude}, ${weather.longitude}", fontSize = 18.sp)
            Text("🕒 Time: ${weather.current_weather.time}", fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text("🌡️ Temperature: ${weather.current_weather.temperature} ${weather.current_weather_units.temperature}", fontSize = 16.sp)
            Text("💨 Windspeed: ${weather.current_weather.windspeed} ${weather.current_weather_units.windspeed}", fontSize = 16.sp)
            Text("🧭 Wind Direction: ${weather.current_weather.winddirection}°", fontSize = 16.sp)
            Text("📍 Elevation: ${weather.elevation}m", fontSize = 16.sp)
            Text("🕓 Interval: ${weather.current_weather.interval} ${weather.current_weather_units.interval}", fontSize = 14.sp)
            Text("🌞 Daytime: ${if (weather.current_weather.is_day.toInt() == 1) "Yes" else "No"}", fontSize = 14.sp)
        }
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
