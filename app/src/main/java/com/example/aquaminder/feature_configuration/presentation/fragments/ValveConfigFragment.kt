package com.example.aquaminder.feature_configuration.presentation.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentValveConfigBinding
import com.example.aquaminder.feature_configuration.presentation.adapter.TimeAdapter
import com.example.aquaminder.feature_configuration.presentation.view_models.ValveConfigViewModel
import com.example.aquaminder.feature_configuration.utils.ValveConfigState
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getHourAndMinute
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getTimeDescription
import com.example.aquaminder.feature_configuration.utils.ValveUtils.parseTimeFromString
import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.FrequencyMode
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

@AndroidEntryPoint
class ValveConfigFragment : Fragment() {

    private val viewModel: ValveConfigViewModel by viewModels()

    private lateinit var binding: FragmentValveConfigBinding

    private lateinit var adapter: TimeAdapter

    private lateinit var checkBoxes: List<Pair<CheckBox, DayOfWeek>>

    private val args: ValveConfigFragmentArgs by navArgs()

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

        setCheckBoxes()

        setMode()
        setWeather()
        setFrequencyMode()
        setIntervalDays()
        setSelectedDays()
        setTimeAdapter()
        setDuration()
        setHumidity()

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
                                setInitialState(
                                    valveConfigState.valve
                                )
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


    @SuppressLint("NewApi")
    private fun setCheckBoxes() {
        checkBoxes = listOf(
            binding.cbL to DayOfWeek.MONDAY,
            binding.cbM to DayOfWeek.TUESDAY,
            binding.cbX to DayOfWeek.WEDNESDAY,
            binding.cbJ to DayOfWeek.THURSDAY,
            binding.cbV to DayOfWeek.FRIDAY,
            binding.cbS to DayOfWeek.SATURDAY,
            binding.cbD to DayOfWeek.SUNDAY
        )
    }

    private fun setHumidity() {
        binding.sliderHumMin.addOnChangeListener { _, value, _ ->
            binding.tvHumMinVal.text = "${value.toInt()}%"
            viewModel.setHumidityMin(value.toInt())
        }

        binding.sliderHumMax.addOnChangeListener { slider, value, _ ->
            binding.tvHumMaxVal.text = "${value.toInt()}%"
            viewModel.setHumidityMax(value.toInt())
        }
    }

    private fun setDuration() {
        binding.etDuration.addTextChangedListener { editable ->
            val value = editable?.toString()?.trim()?.toIntOrNull() ?: 0
            if (value in MIN_DURATION_MINUTES..MAX_DURATION_MINUTES) {
                viewModel.setDuration(value)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun setSelectedDays() {
        checkBoxes.forEach { (checkBox, day) ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.addDayOfWeek(day)
                } else {
                    viewModel.removeDayOfWeek(day)
                }
            }
        }
    }

    private fun setIntervalDays() {
        binding.etEveryNDays.addTextChangedListener { editable ->
            val value = editable?.toString()?.trim()?.toIntOrNull() ?: 0
            if (value in MIN_DAYS..MAX_DAYS) {
                viewModel.setIntervalDays(value)
            }
        }
    }

    private fun setFrequencyMode() {
        binding.rgFrequencyType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbEveryNDays -> {
                    setDaysCheckbox(false)
                    viewModel.setFrequencyMode(FrequencyMode.INTERVAL_DAYS)
                }

                R.id.rbChooseDays -> {
                    setDaysCheckbox(true)
                    viewModel.setFrequencyMode(FrequencyMode.SELECTED_DAYS)
                }
            }
        }
    }

    private fun setDaysCheckbox(isEnabled: Boolean) {
        checkBoxes.forEach { (checkBox, _) ->
            checkBox.isEnabled = isEnabled
        }
    }

    private fun setWeather() {
        binding.switchWeather.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setWeatherChecked(isChecked)
            viewModel.setSwitchSound(isChecked)
        }
    }

    private fun setMode() {
        binding.rgControlMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbScheduled -> {
                    binding.groupScheduled.isVisible = true
                    binding.groupSensor.isVisible = false
                    viewModel.setControlMode(ControlMode.SCHEDULED)
                }

                R.id.rbSensor -> {
                    binding.groupScheduled.isVisible = false
                    binding.groupSensor.isVisible = true
                    viewModel.setControlMode(ControlMode.SENSOR)
                }
            }
        }
    }

    private fun setTimeAdapter() {
        adapter = TimeAdapter(
            times = mutableListOf(),
            onRemove = { removedTime ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fragment_valve_config_removed, removedTime),
                    Toast.LENGTH_SHORT
                ).show()
                parseTimeFromString(removedTime)?.let { viewModel.removeTime(it) }
            },
            onEdit = { time ->
                setTimePicker(time) { pickedTime ->
                    adapter.updateTime(time, getTimeDescription(pickedTime))
                }
            }
        )

        binding.rvTimes.adapter = adapter

        binding.btnPickTime.setOnClickListener {
            setTimePicker { pickedTime ->
                adapter.addTime(getTimeDescription(pickedTime))
                viewModel.addTime(pickedTime)
            }

        }
    }

    private fun setTimePicker(
        oldTime: String? = null,
        onTimePicked: (LocalTime) -> Unit
    ) {
        val (oldHour, oldMinute) = getHourAndMinute(oldTime)
        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime: LocalTime = LocalTime.of(hourOfDay, minute)
                onTimePicked(selectedTime)
            },
            oldHour,
            oldMinute,
            true
        )
        timePicker.show()
    }

    private fun setCreateValveState() {
        setButton(isEdition = false)
        binding.tvTitle.text = getString(R.string.fragment_valve_config_new_title)
    }

    private fun setInitialState(valve: ValveDomainModel) {
        setButton(isEdition = true)
        setValveParameters(valve)
    }

    @SuppressLint("NewApi")
    private fun setValveParameters(valve: ValveDomainModel) {
        binding.tvTitle.text = getString(R.string.fragment_valve_config_title, valve.id.toString())

        when (valve.controlMode) {
            ControlMode.SCHEDULED -> {
                binding.rbScheduled.isChecked = true
                binding.groupScheduled.isVisible = true
                binding.groupSensor.isVisible = false
            }
            ControlMode.SENSOR -> {
                binding.rbSensor.isChecked = true
                binding.groupScheduled.isVisible = false
                binding.groupSensor.isVisible = true
            }
            null -> binding.rbScheduled.isChecked = true
        }

        binding.switchWeather.isChecked = valve.isWeatherChecked

        when (valve.schedule?.frequencyMode) {
            FrequencyMode.SELECTED_DAYS -> {
                binding.rbChooseDays.isChecked = true
                setDaysCheckbox(true)
            }
            FrequencyMode.INTERVAL_DAYS -> {
                binding.rbEveryNDays.isChecked = true
                setDaysCheckbox(false)
            }
            null -> binding.rbEveryNDays.isChecked = true
        }

        binding.etEveryNDays.setText((valve.schedule?.intervalDays ?: 2).toString())

        checkBoxes.forEach { (checkBox, day) ->
            checkBox.isChecked = valve.schedule?.daysOfWeek?.contains(day) ?: false
        }

        valve.schedule?.waterTimes?.forEach { time ->
            adapter.addTime(getTimeDescription(time))
        }

        binding.etDuration.setText((valve.schedule?.duration ?: 3).toString())

        val minHum = valve.humidityMin
        binding.sliderHumMin.value = minHum.toFloat()
        binding.tvHumMinVal.text = "$minHum%"

        val maxHum = valve.humidityMax
        binding.sliderHumMax.value = maxHum.toFloat()
        binding.tvHumMaxVal.text = "$maxHum%"
    }

    private fun setButton(isEdition: Boolean) {
        if (isEdition) {
            binding.btnSave.text = getString(R.string.fragment_valve_config_btn_update)
            binding.btnSave.setOnClickListener {
                viewModel.saveValveConfig()
            }
        } else {
            binding.btnSave.text = getString(R.string.fragment_valve_config_btn_save)
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
        private const val MAX_DURATION_MINUTES = 60

        private const val MIN_DAYS = 1
        private const val MAX_DAYS = 31
    }

}