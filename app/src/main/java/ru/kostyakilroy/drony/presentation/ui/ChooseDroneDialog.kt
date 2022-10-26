package ru.kostyakilroy.drony.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.kostyakilroy.drony.R
import ru.kostyakilroy.drony.databinding.DialogChooseDroneBinding
import ru.kostyakilroy.drony.domain.drone.DJIQuadcopters

const val DRONE_PREF = "DRONE_PREFERENCES"
const val CHOSEN_DRONE = "CHOSEN_DRONE"

class ChooseDroneDialog : DialogFragment() {
    private var _binding: DialogChooseDroneBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChooseDroneBinding.inflate(inflater, container, false)

        val view = binding.root

        preferences = view.context.getSharedPreferences(DRONE_PREF, Context.MODE_PRIVATE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Записывает имя текущего дрона в SharedPreferences
        val currentChosenDrone = preferences.getString(CHOSEN_DRONE,  DJIQuadcopters.AIR2S.name)

        binding.chooseDroneRadioGroup.check(
            when(currentChosenDrone) {
                DJIQuadcopters.MAVIC3.name -> R.id.choose_drone_djimavic3
                DJIQuadcopters.MINI2.name -> R.id.choose_drone_djimini2
                else -> R.id.choose_drone_djiair2s
            }
        )
        binding.toolbar.setNavigationOnClickListener { dismiss() }
        binding.toolbar.title = "Choose a drone"
        binding.toolbar.inflateMenu(R.menu.choose_drone)
        binding.toolbar.setOnMenuItemClickListener {
            // Обновляет имя текущего дрона в SharedPreferences
            when(binding.chooseDroneRadioGroup.checkedRadioButtonId) {
                R.id.choose_drone_djiair2s -> preferences.edit().putString(CHOSEN_DRONE, DJIQuadcopters.AIR2S.name).apply()
                R.id.choose_drone_djimavic3 -> preferences.edit().putString(CHOSEN_DRONE, DJIQuadcopters.MAVIC3.name).apply()
                R.id.choose_drone_djimini2 -> preferences.edit().putString(CHOSEN_DRONE, DJIQuadcopters.MINI2.name).apply()
            }
            dismiss()
            true
        }

    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.window!!.let {
//            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        }
//        return dialog
//    }

//    override fun onStart() {
//        super.onStart()
//        val dialog = this.dialog
//        if (dialog != null) {
//            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        }
//    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}