package ru.kostyakilroy.drony.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kostyakilroy.drony.R
import ru.kostyakilroy.drony.presentation.viewmodels.ForecastDetailsViewModel

class ForecastDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ForecastDetailsFragment()
    }

    private lateinit var viewModel: ForecastDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast_details, container, false)

        return view
    }


}