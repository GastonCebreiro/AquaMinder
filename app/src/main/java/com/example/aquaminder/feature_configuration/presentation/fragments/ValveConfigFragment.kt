package com.example.aquaminder.feature_configuration.presentation.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentValveConfigBinding
import com.example.aquaminder.databinding.ItemSpinnerDropdownBinding
import com.example.aquaminder.databinding.ItemSpinnerSelectedBinding
import com.example.aquaminder.feature_configuration.presentation.adapter.TimeAdapter
import com.example.aquaminder.feature_configuration.presentation.view_models.ValveConfigViewModel
import com.example.aquaminder.feature_configuration.utils.ValveConfigState
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getHourAndMinute
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getTimeDescription
import com.example.aquaminder.feature_configuration.utils.ValveUtils.parseTimeFromString
import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.FrequencyMode
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValveConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getIrrigationZoneConfiguration()

        setButton()
        setCheckBoxes()
        setMode()
        setActive()
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
                            is ValveConfigState.Success -> {
                                setValveSelector(valveConfigState.configuration.valves)
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

    private fun setValveSelector(valves: List<ValveDomainModel>) {
        if (valves.isEmpty()) {
            return
        }

        val valvesDescription = valves.map { valve ->
            getString(R.string.fragment_home_valve_description, valve.id.toString())
        }

        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner_selected,
            valvesDescription
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val binding = if (convertView == null) {
                    ItemSpinnerSelectedBinding.inflate(LayoutInflater.from(context), parent, false)
                } else {
                    ItemSpinnerSelectedBinding.bind(convertView)
                }

                binding.tvSpinnerSelected.text = getItem(position)

                return binding.root
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val binding = if (convertView == null) {
                    ItemSpinnerDropdownBinding.inflate(LayoutInflater.from(context), parent, false)
                } else {
                    ItemSpinnerDropdownBinding.bind(convertView)
                }

                binding.tvSpinnerDropdown.text = getItem(position)
                return binding.root
            }
        }

        binding.spValves.adapter = adapter

        binding.spValves.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedValve = viewModel.getSelectedValve(position)
                setValveParameters(selectedValve)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
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
            when {
                value < MIN_DURATION_MINUTES -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.fragment_valve_config_min_duration, MIN_DURATION_MINUTES.toString()),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.setDuration(MIN_DURATION_MINUTES)
                }
                value > MAX_DURATION_MINUTES -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.fragment_valve_config_max_duration, MAX_DURATION_MINUTES.toString()),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.setDuration(MAX_DURATION_MINUTES)
                }
                else -> viewModel.setDuration(value)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun setSelectedDays() {
        checkBoxes.forEach { (checkBox, day) ->
            checkBox.setOnCheckedChangeListener(null)
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
        binding.rgFrequencyType.setOnCheckedChangeListener(null)

        val rbEveryNDays = binding.rbEveryNDays
        val rbChooseDays = binding.rbChooseDays

        val listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (buttonView.id) {
                    R.id.rbEveryNDays -> {
                        rbChooseDays.isChecked = false
                        rbEveryNDays.isChecked = true
                        setDaysCheckbox(false)
                        viewModel.setFrequencyMode(FrequencyMode.INTERVAL_DAYS)
                    }

                    R.id.rbChooseDays -> {
                        rbEveryNDays.isChecked = false
                        rbChooseDays.isChecked = true
                        setDaysCheckbox(true)
                        viewModel.setFrequencyMode(FrequencyMode.SELECTED_DAYS)
                    }
                }
            }
        }

        rbEveryNDays.setOnCheckedChangeListener(listener)
        rbChooseDays.setOnCheckedChangeListener(listener)
    }

    private fun setDaysCheckbox(isEnabled: Boolean) {
        checkBoxes.forEach { (checkBox, _) ->
            checkBox.isEnabled = isEnabled
        }
    }

    private fun setActive() {
        binding.switchActive.setOnCheckedChangeListener { _, isChecked ->
            binding.tvActiveState.text = if (isChecked)
                getString(R.string.fragment_configuration_switch_on)
            else
                getString(R.string.fragment_configuration_switch_off)

            binding.tvActiveState.setTextColor(
                if (isChecked)
                    ContextCompat.getColor(requireContext(), R.color.light_blue)
                else
                    ContextCompat.getColor(requireContext(), R.color.gray_delete)
            )

            viewModel.setActiveChecked(isChecked)
            viewModel.setSwitchSound(isChecked)
        }
    }


    private fun setWeather() {
        binding.switchWeather.setOnCheckedChangeListener { _, isChecked ->
            binding.tvWeatherState.text = if (isChecked)
                getString(R.string.fragment_configuration_switch_on)
            else
                getString(R.string.fragment_configuration_switch_off)

            binding.tvWeatherState.setTextColor(
                if (isChecked)
                    ContextCompat.getColor(requireContext(), R.color.light_blue)
                else
                    ContextCompat.getColor(requireContext(), R.color.gray_delete)
            )

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

        binding.rvTimes.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
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

    @SuppressLint("NewApi")
    private fun setValveParameters(valve: ValveDomainModel) {
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
        binding.switchActive.isChecked = valve.isActive

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
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = valve.schedule?.daysOfWeek?.contains(day) ?: false
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.addDayOfWeek(day)
                } else {
                    viewModel.removeDayOfWeek(day)
                }
            }
        }

        adapter.cleanTimes()
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

    private fun setButton() {
        binding.btnSave.setOnClickListener {
            viewModel.saveValveConfig()
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