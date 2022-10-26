package ru.kostyakilroy.drony.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kostyakilroy.drony.data.location.DefaultLocationProvider
import ru.kostyakilroy.drony.domain.location.LocationProvider
import ru.kostyakilroy.drony.data.repository.WeatherRepositoryImpl
import ru.kostyakilroy.drony.domain.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(
        locationProviderClient: DefaultLocationProvider
    ): LocationProvider

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        repository: WeatherRepositoryImpl
    ): WeatherRepository
}