package ru.kostyakilroy.drony.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.kostyakilroy.drony.domain.weather.Weather
import ru.kostyakilroy.drony.domain.location.LocationProvider
import ru.kostyakilroy.drony.domain.repository.WeatherRepository
import ru.kostyakilroy.drony.domain.usecases.GetCurrentDroneUseCase
import ru.kostyakilroy.drony.domain.usecases.GetFlyConditionsUseCase
import ru.kostyakilroy.drony.domain.util.Resource
import ru.kostyakilroy.drony.presentation.state.FlyConditionsUiState
import ru.kostyakilroy.drony.presentation.state.LocationState
import ru.kostyakilroy.drony.presentation.state.WeatherUiState
import javax.inject.Inject

@HiltViewModel
class ForecastMainViewModel @Inject constructor(
//    @Assisted("currentDrone") private val drone: String,
    private val repository: WeatherRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {


    private val flyConditionsUseCase = GetFlyConditionsUseCase()
    private val currentDroneUseCase = GetCurrentDroneUseCase()

    private var _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var _flyConditionsState = MutableStateFlow(FlyConditionsUiState())
    val flyConditionsState: StateFlow<FlyConditionsUiState> = _flyConditionsState.asStateFlow()

    // TODO
    private var _latNlon = MutableStateFlow(LocationState())
    val latNlon: StateFlow<LocationState> = _latNlon.asStateFlow()


    private val defaultDrone = currentDroneUseCase.execute(null)
//    private val _currentDrone = MutableLiveData<Quadcopter>(defaultDrone)
    private var _currentDrone = defaultDrone


    // Получение текущей локации и загрузка данных из ДБ/API
    fun loadCurrentWeatherData() {
        viewModelScope.launch() {

            _uiState.update { it.copy(isLoading = true) }

            val simpleLocation = locationProvider.getCurrentLocation()
            when (simpleLocation) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val location = simpleLocation.data!!
                    getHourlyWeather(location.latitude, location.longitude)
                    _latNlon.update {
                        it.copy(latitude = simpleLocation.data.latitude, longitude = simpleLocation.data.longitude)
                    }
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, error = simpleLocation.message)
                }
            }
        }
    }

    // Получаем имя дрона из SharedPreferences
    fun setCurrentDrone(droneName: String){
        _currentDrone = currentDroneUseCase.execute(droneName)
    }

    // Обновляет состояние? полетных условий
    fun updateFlyConditions(weatherData: Weather) {
        viewModelScope.launch {
            _flyConditionsState.update {
                it.copy(flyConditions = flyConditionsUseCase.execute(weather = weatherData, _currentDrone))
            }
        }
    }


    // Получение и загрузка данных из ДБ/API на основе полученной ранее локации
    private fun getHourlyWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.getHourlyWeather(latitude, longitude).onEach { result ->
                when(result) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val weather = result.data
                        val flyConditions = flyConditionsUseCase.execute(weather!!.first(), _currentDrone)
                        _uiState.update {
                            it.copy(
                                weatherInfo = weather,
                                isLoading = false,
                                error = null
                            )
                        }
                        _flyConditionsState.update {
                            it.copy(flyConditions = flyConditions)
                        }
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(isLoading = false, error = result.message)
                    }
                }
            }.launchIn(this)
        }
    }
}


//class ForecastMainViewModelFactory @AssistedInject constructor(
//    @Assisted("currentDrone") private val drone: String,
//    private val repository: WeatherRepository,
//    private val locationProvider: LocationProvider
//) : ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        require(modelClass == ForecastMainViewModel::class)
//        return ForecastMainViewModel(drone, repository, locationProvider) as T
//    }
//
//    @AssistedFactory
//    interface Factory {
//        fun create(@Assisted("currentDrone") drone: String): ForecastMainViewModel
//    }
//}