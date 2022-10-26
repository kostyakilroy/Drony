package ru.kostyakilroy.drony.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.kostyakilroy.drony.data.local.HourlyWeatherDao
import ru.kostyakilroy.drony.data.local.HourlyWeatherDatabase
import ru.kostyakilroy.drony.data.remote.WeatherAPI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): WeatherAPI{
        return Retrofit.Builder().baseUrl("https://api.tomorrow.io/v4/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient{
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext app: Context): HourlyWeatherDatabase {
        return Room.databaseBuilder(
            app,
            HourlyWeatherDatabase::class.java,
            "hourly_weather"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHourlyWeatherDao(db: HourlyWeatherDatabase): HourlyWeatherDao {
        return db.hourlyWeatherDao()
    }
}