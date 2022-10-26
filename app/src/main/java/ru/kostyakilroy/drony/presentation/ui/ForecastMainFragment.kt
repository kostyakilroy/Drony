package ru.kostyakilroy.drony.presentation.ui

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kostyakilroy.drony.R
import ru.kostyakilroy.drony.databinding.FragmentForecastMainBinding
import ru.kostyakilroy.drony.domain.drone.DJIQuadcopters
import ru.kostyakilroy.drony.domain.drone.FlyConditions
import ru.kostyakilroy.drony.domain.weather.Weather
import ru.kostyakilroy.drony.presentation.viewmodels.ForecastMainViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ForecastMainFragment : Fragment() {

    private var _binding: FragmentForecastMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var geocoder: Geocoder
    private lateinit var supportFragmentManager: FragmentManager

    private lateinit var preferences: SharedPreferences

    private val viewModel: ForecastMainViewModel by viewModels()

//    private val viewModel: ForecastMainViewModel by viewModels {
//        factory.create(currentDrone)
//    }
//
//    @Inject
//    lateinit var factory: ForecastMainViewModelFactory.Factory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastMainBinding.inflate(inflater, container, false)
        val view = binding.root

        geocoder = Geocoder(requireContext())

        preferences = requireContext().getSharedPreferences(DRONE_PREF, Context.MODE_PRIVATE)

        // TODO
        // Получение имя дрона из SharedPreferences
        // Обновления текущего дрона во ViewModel
        val chosenDrone = preferences.getString(CHOSEN_DRONE, DJIQuadcopters.AIR2S.name)
        val drone = DJIQuadcopters.valueOf(chosenDrone ?: DJIQuadcopters.AIR2S.name)
        viewModel.setCurrentDrone(chosenDrone ?: DJIQuadcopters.AIR2S.name)
        binding.fcMainDroneName.text = getString(drone.quadcopter.name)


        // Загрузка данных во ViewModel
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadCurrentWeatherData()
        }

        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

        // Получение данных из ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.apply {

                        // Скрыть "таблички" с информацией( temp, wind, etc)
                        weatherCards.fcCardsGroup.visibility = if (uiState.isLoading || uiState.error != null) View.INVISIBLE else View.VISIBLE
                        // Показать/скрыть прогрессбар
                        fcMainErrorContainer.progressCircular.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE

                        if (uiState.weatherInfo != null) {
                            val weatherData =  uiState.weatherInfo.first()

                            // Установка иконки и наименования текущей погоды
                            fcMainAnimationContainerWeather.setImageResource(weatherData.weatherType.iconRes)
                            fcMainAnimationContainerWeatherType.text = weatherData.weatherType.typeName

                            // Обновить информацию в "таблчиках"
                            updateWeatherCardValues(weatherData)

                            // todo add cancellation
                            // Обновляет текущий дрон и условия для полёта во ViewModel,
                            // когда в ChooseDroneDialog пользователь выбирает новый дрон
                            preferences.registerOnSharedPreferenceChangeListener{ prf, key ->
                                val currentDrone = preferences.getString(CHOSEN_DRONE, DJIQuadcopters.AIR2S.name)
                                val drone1 = DJIQuadcopters.valueOf(currentDrone ?: DJIQuadcopters.AIR2S.name)
                                viewModel.setCurrentDrone(currentDrone ?: DJIQuadcopters.AIR2S.name)
                                viewModel.updateFlyConditions(weatherData)
                                fcMainDroneName.text = getText(drone1.quadcopter.name)
                            }

                        }
                        if (uiState.error != null){
//                            weatherCards.fcCardsGroup.visibility = View.INVISIBLE
                            fcMainErrorContainer.retryContainer.visibility = if (uiState.isLoading) View.GONE else View.VISIBLE
                            fcMainErrorContainer.message.text = uiState.error
                            fcMainErrorContainer.retryButton.setOnClickListener { viewModel.loadCurrentWeatherData() }
                        }
                    }
                }
            }
        }


        // Устанавливает имя текущего местоположения в тулбаре
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latNlon.collect{ latNlon ->
                    if(latNlon.latitude != null && latNlon.longitude != null) {

                        binding.fcMainToolbarLocationName.text = getLocationName(latNlon.latitude, latNlon.longitude)}
//                        binding.fcMainToolbarTv.text = "${latNlon.latitude}, ${latNlon.longitude})"}
                }
            }
        }

        // Обновляет цвет "табличек" при выборе нового дрона
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flyConditionsState.collect{ cond ->
                    if(cond.flyConditions != null) {
                        updateWeatherCardConditions(cond.flyConditions)
                    }
                }
            }
        }

        // TODO
        binding.fcMainToolbarLastUpdated.text = "Обновлено " + ZonedDateTime.now().format(DateTimeFormatter.ISO_TIME)

        binding.fcMainToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_test) {
                Toast.makeText(requireActivity(), "Will be added soon!", Toast.LENGTH_SHORT).show()
                true
            } else false

        }


        binding.weatherCards.temperature.setOnClickListener {
            showWeatherCardExplanationDialog(getString(R.string.temperature), getString(R.string.temperatureExplanation))
        }
        binding.weatherCards.precipitation.setOnClickListener {
            showWeatherCardExplanationDialog(getString(R.string.precipitations), getString(R.string.precipitationsExplanation))
        }
        binding.weatherCards.wind.setOnClickListener {
            showWeatherCardExplanationDialog(getString(R.string.wind), getString(R.string.windExplanation))
        }
        binding.weatherCards.windGust.setOnClickListener {
            showWeatherCardExplanationDialog(getString(R.string.windGust), getString(R.string.windExplanation))
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO
        val navCont = findNavController()

        binding.fcMainToolbarContainer.setOnClickListener {
            navCont.navigate(R.id.action_forecastMainFragment_to_locationActionDialogFragment)
        }

        binding.fcMainDroneName.setOnClickListener {
            navCont.navigate(R.id.action_forecastMainFragment_to_chooseDroneFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // TODO
    // Получает название местоположения
    private fun getLocationName(lat: Double, lon: Double): String {
        var geoName = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lon, 1) {
                geoName = it.first().locality + ", " + it.first().adminArea
            }
        } else {
            val addressList = geocoder.getFromLocation(lat, lon, 1)
            geoName = addressList!!.first().locality + ", " + addressList.first().adminArea
        }
        return geoName
    }

    // Диалог показываемый при нажатии на одно из значений
    private fun showWeatherCardExplanationDialog(title: String, explanation: String) {
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
        alertDialog
            .setCancelable(true)
            .setTitle(title)
            .setMessage(explanation)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    // Обновляет значения "табличек" при обновлении данных во ViewModel
    private fun updateWeatherCardValues(weatherData: Weather) {
        binding.apply {
            weatherCards.apply {
                temperatureValue.text = getString(R.string.temperatureValue, weatherData.temperature)
                precipitationValue.text = getString(R.string.precipitationValue, weatherData.precipitation)
                windValue.text = getString(R.string.windValue, weatherData.windSpeed)
                windGustValue.text = getString(R.string.windGustValue, weatherData.windGust)
                cloudsValue.text = getString(R.string.cloudsValue, weatherData.cloudCover)
                visibilityValue.text = getString(R.string.visibilityValue, weatherData.visibility)
            }

        }
    }

    // Обновляет цвет "табличек" при обновлении данных во ViewModel
    private fun updateWeatherCardConditions(flyConditions: FlyConditions) {
        binding.apply {
            weatherCards.apply {
                temperature.setCardBackgroundColor(
                    resources.getColor(flyConditions.temperature.color, null)
                )
                precipitation.setCardBackgroundColor(
                    resources.getColor(flyConditions.precipitation.color, null)
                )
                wind.setCardBackgroundColor(
                    resources.getColor(flyConditions.wind.color, null)
                )
                windGust.setCardBackgroundColor(
                    resources.getColor(flyConditions.windGust.color, null
                    )
                )
                clouds.setCardBackgroundColor(
                    resources.getColor(flyConditions.clouds.color, null)
                )
                visibility.setCardBackgroundColor(
                    resources.getColor(flyConditions.visibility.color, null)
                )
            }

        }
    }

}