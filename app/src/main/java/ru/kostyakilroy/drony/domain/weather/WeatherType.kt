package ru.kostyakilroy.drony.domain.weather

import androidx.annotation.DrawableRes
import ru.kostyakilroy.drony.R

//enum class WeatherType(val code: Int,
//                       val weatherType: String,
//                       @DrawableRes val iconRes: Int
//) {
//    Unknown(0, "Unknown", R.drawable.ic_weather_tstorm),
//    ClearSunny(1000, "Clear, Sunny", R.drawable.ic_weather_clear_day),
//    MostlyClear(1100, "Mostly Clear", R.drawable.ic_weather_mostly_clear_day),
//    PartlyCloudy(1101, "Partly Cloudy", R.drawable.ic_weather_partly_cloudy_day),
//    MostlyCloudy(1102, "Mostly Cloudy", R.drawable.ic_weather_mostly_cloudy),
//    Cloudy(1001, "Cloudy", R.drawable.ic_weather_cloudy),
//    Fog(2000, "Fog", R.drawable.ic_weather_fog),
//    LightFog(2100, "Light Fog", R.drawable.ic_weather_fog_light),
//    Drizzle(4000, "Drizzle", R.drawable.ic_weather_drizzle),
//    Rain(4001, "Rain", R.drawable.ic_weather_rain),
//    LightRain(4200, "Light Rain", R.drawable.ic_weather_rain_light),
//    HeavyRain(4201, "Heavy Rain", R.drawable.ic_weather_rain_heavy),
//    Snow(5000, "Snow", R.drawable.ic_weather_snow),
//    Flurries(5001, "Flurries", R.drawable.ic_weather_flurries),
//    LightSnow(5100, "Light Snow", R.drawable.ic_weather_snow_light),
//    HeavySnow(5101, "Heavy Snow", R.drawable.ic_weather_snow_heavy),
//    FreezingDrizzle(6000, "Freezing Drizzle", R.drawable.ic_weather_freezing_drizzle),
//    FreezingRain(6001, "Freezing Rain", R.drawable.ic_weather_freezing_rain),
//    LightFreezingRain(6200, "Light Freezing Rain", R.drawable.ic_weather_freezing_rain_light),
//    HeavyFreezingRain(6201, "Heavy Freezing Rain", R.drawable.ic_weather_freezing_rain_heavy),
//    IcePellets(7000, "Ice Pellets", R.drawable.ic_weather_ice_pellets),
//    HeavyIcePellets(7101, "Heavy Ice Pellets", R.drawable.ic_weather_ice_pellets_heavy),
//    LightIcePellets(7102, "Light Ice Pellets", R.drawable.ic_weather_ice_pellets_light),
//    Thunderstorm(8000, "Thunderstorm", R.drawable.ic_weather_tstorm)
//}

sealed class WeatherType(
    val typeName: String,
    @DrawableRes val iconRes: Int
) {
    object Unknown : WeatherType("Unknown", R.drawable.ic_weather_tstorm)
    object ClearSunny : WeatherType("Clear, Sunny", R.drawable.ic_weather_clear_day)
    object MostlyClear : WeatherType("Mostly Clear", R.drawable.ic_weather_mostly_clear_day)
    object PartlyCloudy : WeatherType("Partly Cloudy", R.drawable.ic_weather_partly_cloudy_day)
    object MostlyCloudy : WeatherType("Mostly Cloudy", R.drawable.ic_weather_mostly_cloudy)
    object Cloudy : WeatherType("Cloudy", R.drawable.ic_weather_cloudy)
    object Fog : WeatherType("Fog", R.drawable.ic_weather_fog)
    object LightFog : WeatherType("Light Fog", R.drawable.ic_weather_fog_light)
    object Drizzle : WeatherType("Drizzle", R.drawable.ic_weather_drizzle)
    object Rain : WeatherType("Rain", R.drawable.ic_weather_rain)
    object LightRain : WeatherType("Light Rain", R.drawable.ic_weather_rain_light)
    object HeavyRain : WeatherType("Heavy Rain", R.drawable.ic_weather_rain_heavy)
    object Snow : WeatherType("Snow", R.drawable.ic_weather_snow)
    object Flurries : WeatherType("Flurries", R.drawable.ic_weather_flurries)
    object LightSnow : WeatherType("Light Snow", R.drawable.ic_weather_snow_light)
    object HeavySnow : WeatherType("Heavy Snow", R.drawable.ic_weather_snow_heavy)
    object FreezingDrizzle : WeatherType("Freezing Drizzle", R.drawable.ic_weather_freezing_drizzle)
    object FreezingRain : WeatherType("Freezing Rain", R.drawable.ic_weather_freezing_rain)
    object LightFreezingRain : WeatherType("Light Freezing Rain", R.drawable.ic_weather_freezing_rain_light)
    object HeavyFreezingRain : WeatherType("Heavy Freezing Rain", R.drawable.ic_weather_freezing_rain_heavy)
    object IcePellets : WeatherType("Ice Pellets", R.drawable.ic_weather_ice_pellets)
    object HeavyIcePellets : WeatherType("Heavy Ice Pellets", R.drawable.ic_weather_ice_pellets_heavy)
    object LightIcePellets : WeatherType("Light Ice Pellets", R.drawable.ic_weather_ice_pellets_light)
    object Thunderstorm : WeatherType("Thunderstorm", R.drawable.ic_weather_tstorm)

    companion object {
        fun fromCodeToWeatherType(code: Int): WeatherType {
            return when(code) {
                1000 -> ClearSunny
                1100 -> MostlyClear
                1101 -> PartlyCloudy
                1102 -> MostlyCloudy
                1001 -> Cloudy
                2000 -> Fog
                2100 -> LightFog
                4000 -> Drizzle
                4001 -> Rain
                4200 -> LightRain
                4201 -> HeavyRain
                5000 -> Snow
                5001 -> Flurries
                5100 -> LightSnow
                5101 -> HeavySnow
                6000 -> FreezingDrizzle
                6001 -> FreezingRain
                6200 -> LightFreezingRain
                6201 -> HeavyFreezingRain
                7000 -> IcePellets
                7101 -> HeavyIcePellets
                7102 -> LightIcePellets
                8000 -> Thunderstorm
                else -> Unknown
            }
        }
    }
}