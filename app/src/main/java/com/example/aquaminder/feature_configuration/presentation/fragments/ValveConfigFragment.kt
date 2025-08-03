package com.example.aquaminder.feature_configuration.presentation.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentValveConfigBinding
import com.example.aquaminder.feature_configuration.presentation.view_models.ValveConfigViewModel
import com.example.aquaminder.feature_configuration.utils.InputFilterMinMax
import com.example.aquaminder.feature_configuration.utils.ValveConfigState
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getHumidityDescription
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getIntervalHoursDescription
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getStartTimeDescription
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalTime

@AndroidEntryPoint
class ValveConfigFragment : Fragment() {

    private val viewModel: ValveConfigViewModel by viewModels()

    private lateinit var binding: FragmentValveConfigBinding

    private val args: ValveConfigFragmentArgs by navArgs()

    private var humidityChangeListener: Slider.OnChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValveConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val valve = args.valve
        viewModel.setSelectedValve(valve)

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.includeProgressBar.clProgressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.valveConfigState.collect { valveConfigState ->
                        when (valveConfigState) {
                            is ValveConfigState.NewValve -> {
                                setCreateValveState()
                            }

                            is ValveConfigState.EditValve -> {
                                setInitialState(valveConfigState.valve, isModified = valveConfigState.isModified)
                            }

                            is ValveConfigState.Error -> {
                                showErrorMessage(valveConfigState.errorMsg)
                            }

                            is ValveConfigState.Idle -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setCreateValveState() {
        setButton(isEdition = false)
        binding.tvTitle.text = getString(R.string.fragment_valve_config_new_title)
    }

    private fun setInitialState(valve: ValveDomainModel, isModified: Boolean) {
        setButton(isEdition = true, isModified)
        setValveParameters(valve)
    }

    private fun setValveParameters(valve: ValveDomainModel) {
        binding.tvTitle.text = getString(R.string.fragment_valve_config_title, valve.id.toString())

        binding.tvHumidity.text = getHumidityDescription(valve.humidity)

        humidityChangeListener?.let { binding.sliderHumidity.removeOnChangeListener(it) }

        binding.sliderHumidity.valueFrom = 0f
        binding.sliderHumidity.valueTo = 100f
        binding.sliderHumidity.stepSize = 1f
        binding.sliderHumidity.value = valve.humidity.toFloat()

        humidityChangeListener = Slider.OnChangeListener { _, value, _ ->
            binding.tvHumidity.text = getHumidityDescription(value.toInt())
            viewModel.setHumidity(value.toInt())
        }
        humidityChangeListener?.let { binding.sliderHumidity.addOnChangeListener(it) }

        binding.tvStartHour.text = getStartTimeDescription(valve.schedule?.startTime)
        binding.tvStartHour.setOnClickListener {
            val initialTime = valve.schedule?.startTime ?: LocalTime.of(0, 0)

            val timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val selectedTime: LocalTime = LocalTime.of(hourOfDay, minute)
                    binding.tvStartHour.text = getStartTimeDescription(selectedTime)
                    viewModel.setStartTime(selectedTime)
                },
                initialTime.hour,
                initialTime.minute,
                true
            )

            timePicker.show()
        }


        binding.tvIntervalHours.text = getIntervalHoursDescription(valve.schedule?.intervalHours)
        binding.btnPlus.setOnClickListener {
            val newIntervalHours = (valve.schedule?.intervalHours ?: 0) + 1
            viewModel.setIntervalHours(newIntervalHours)
            binding.tvIntervalHours.text = getIntervalHoursDescription(newIntervalHours)
        }
        binding.btnMinus.setOnClickListener {
            val newIntervalHours = (valve.schedule?.intervalHours ?: 0) - 1
            if (newIntervalHours > 0) {
                viewModel.setIntervalHours(newIntervalHours)
                binding.tvIntervalHours.text = getIntervalHoursDescription(newIntervalHours)
            }
        }

        binding.etDuration.setText((valve.schedule?.duration).toString())

        binding.etDuration.filters = arrayOf(InputFilterMinMax(MIN_DURATION_MINUTES, MAX_DURATION_MINUTES))
        binding.etDuration.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val value = s.toString().toIntOrNull()
                binding.etDuration.setSelection(binding.etDuration.text.length)
                if (value != null) {
                    viewModel.setDuration(value)
                }
            }
        })
    }

    private fun setButton(isEdition: Boolean, isModified: Boolean = false) {
        if (isEdition) {
            binding.btnSave.text = getString(R.string.fragment_valve_config_btn_update)
            binding.btnSave.isEnabled = isModified
            binding.btnSave.setOnClickListener {
                viewModel.saveValveConfig()
            }
        } else {
            binding.btnSave.text = getString(R.string.fragment_valve_config_btn_save)
            binding.btnSave.isEnabled = true
            binding.btnSave.setOnClickListener {
                viewModel.createNewValve()
            }
        }
    }

    private fun showErrorMessage(message: String, logoId: Int? = null) {
        DialogUtils.showErrorDialog(
            context = requireContext(),
            imageId = logoId,
            titleText = message,
            onAcceptAction = {
            }
        )
    }

    companion object {
        private const val MIN_DURATION_MINUTES = 1
        private const val MAX_DURATION_MINUTES = 59
    }

}