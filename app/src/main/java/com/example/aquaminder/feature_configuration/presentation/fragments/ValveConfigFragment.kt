package com.example.aquaminder.feature_configuration.presentation.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.res.ColorStateList
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

    // Para evitar loops de checkbox All
    private var isBulkUpdating = false

    private var isDurationInternalUpdate = false
    private var durationType: DurationType = DurationType.SECONDS

    private enum class DurationType { MINUTES, SECONDS }

    private var isIntervalInternalUpdate = false

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
        setDurationInput()
        setDurationTypeSelector()
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
                                showErrorMessage(valveConfigState.errorMsg, valveConfigState.logoId)
                            }

                            is ValveConfigState.Idle -> {}
                            is ValveConfigState.ConfigSaved -> {
                                showConfigSavedMessage()
                                viewModel.restartState()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showConfigSavedMessage() {
        Toast.makeText(
            requireContext(),
            getString(R.string.fragment_valve_config_saved),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setValveSelector(valves: List<ValveDomainModel>) {
        if (valves.isEmpty()) {
            return
        }

        val valvesDescription = List(valves.size) { index ->
            getString(
                R.string.fragment_home_valve_description,
                (index + 1).toString()
            )
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

    private fun setDurationInput() {
        binding.etDuration.addTextChangedListener { editable ->
            if (isDurationInternalUpdate) return@addTextChangedListener

            val value = editable?.toString()?.trim()?.toIntOrNull() ?: return@addTextChangedListener
            val fixedValue = value.coerceIn(MIN_DURATION_MINUTES, MAX_DURATION_MINUTES)

            if (fixedValue != value) {
                isDurationInternalUpdate = true
                binding.etDuration.setText(fixedValue.toString())
                binding.etDuration.setSelection(fixedValue.toString().length)
                isDurationInternalUpdate = false
            }

            val durationInSeconds = when (durationType) {
                DurationType.MINUTES -> fixedValue * 60
                DurationType.SECONDS -> fixedValue
            }

            viewModel.setDuration(durationInSeconds)
        }
    }


    private fun setDurationTypeSelector() {
        binding.rgTimeUnit.setOnCheckedChangeListener { _, checkedId ->

            if (isDurationInternalUpdate) return@setOnCheckedChangeListener

            val currentValue = binding.etDuration.text.toString().toIntOrNull() ?: return@setOnCheckedChangeListener

            durationType = when (checkedId) {
                R.id.rbMin -> DurationType.MINUTES
                R.id.rbSeg -> DurationType.SECONDS
                else -> DurationType.SECONDS
            }

            val durationInSeconds = when (durationType) {
                DurationType.MINUTES -> currentValue * 60
                DurationType.SECONDS -> currentValue
            }

            viewModel.setDuration(durationInSeconds)
        }
    }

    @SuppressLint("NewApi")
    private fun setSelectedDays() {

        binding.cbAll.setOnCheckedChangeListener(null)

        binding.cbAll.setOnCheckedChangeListener { _, isChecked ->
            if (isBulkUpdating) return@setOnCheckedChangeListener

            isBulkUpdating = true

            if (isChecked) {
                checkBoxes.forEach { (checkBox, day) ->
                    checkBox.isChecked = true
                    viewModel.addDayOfWeek(day)
                }
            } else {
                checkBoxes.forEach { (checkBox, day) ->
                    checkBox.isChecked = false
                    viewModel.removeDayOfWeek(day)
                }
            }

            isBulkUpdating = false
        }
    }

    @SuppressLint("NewApi")
    private fun updateAllCheck() {
        val allChecked = checkBoxes.all { (cb, _) -> cb.isChecked }

        isBulkUpdating = true
        binding.cbAll.isChecked = allChecked
        isBulkUpdating = false
    }


//    private fun setIntervalDays() {
//        binding.etEveryNDays.addTextChangedListener { editable ->
//            val value = editable?.toString()?.trim()?.toIntOrNull() ?: 0
//            if (value in MIN_DAYS..MAX_DAYS) {
//                viewModel.setIntervalDays(value)
//            }
//        }
//    }

    private fun setIntervalDays() {
        binding.etEveryNDays.addTextChangedListener { editable ->
            if (isIntervalInternalUpdate) return@addTextChangedListener

            val input = editable?.toString()?.trim()
            val value = input?.toIntOrNull() ?: return@addTextChangedListener

            val clamped = value.coerceIn(MIN_DAYS, MAX_DAYS)

            if (clamped.toString() != input) {
                isIntervalInternalUpdate = true
                binding.etEveryNDays.setText(clamped.toString())
                binding.etEveryNDays.setSelection(clamped.toString().length)
                isIntervalInternalUpdate = false
            }

            viewModel.setIntervalDays(clamped)
        }
    }

    private fun setFrequencyMode() {
        binding.rgFrequencyType.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.rbEveryNDays -> {
                    setFrequencyView(FrequencyMode.INTERVAL_DAYS)
                    viewModel.setFrequencyMode(FrequencyMode.INTERVAL_DAYS)
                }

                R.id.rbChooseDays -> {
                    setFrequencyView(FrequencyMode.SELECTED_DAYS)
                    viewModel.setFrequencyMode(FrequencyMode.SELECTED_DAYS)
                }
            }
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

            binding.clActive.background = ContextCompat.getDrawable(requireContext(),
                if (isChecked) R.drawable.background_switch_selector
                else R.drawable.background_switch_selector_off
            )

            binding.ivActive.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                if (isChecked) R.color.light_blue
                else R.color.gray_delete
            ))

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

            binding.clWeather.background = ContextCompat.getDrawable(requireContext(),
                if (isChecked) R.drawable.background_switch_selector
                else R.drawable.background_switch_selector_off
            )

            binding.ivWeather.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                if (isChecked) R.color.light_blue
                else R.color.gray_delete
            ))

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
                    setFrequencyView(viewModel.actualValve.schedule?.frequencyMode)
                }

                R.id.rbSensor -> {
                    binding.groupScheduled.isVisible = false
                    binding.groupSensor.isVisible = true
                    binding.flowDaysOfWeek.visibility = View.INVISIBLE
                    binding.clEveryNDays.visibility = View.INVISIBLE
                    binding.cbAll.visibility = View.INVISIBLE
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
                    parseTimeFromString(time)?.let { oldLocalTime ->
                        viewModel.updateTime(oldLocalTime, pickedTime)
                    }
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

        when (valve.schedule?.frequencyMode) {
            FrequencyMode.SELECTED_DAYS -> {
                binding.rbChooseDays.isChecked = true
                setFrequencyView(FrequencyMode.SELECTED_DAYS)
            }

            FrequencyMode.INTERVAL_DAYS -> {
                binding.rbEveryNDays.isChecked = true
                setFrequencyView(FrequencyMode.INTERVAL_DAYS)
            }

            null -> binding.rbEveryNDays.isChecked = true
        }

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
                binding.flowDaysOfWeek.visibility = View.INVISIBLE
                binding.clEveryNDays.visibility = View.INVISIBLE
                binding.cbAll.visibility = View.INVISIBLE
            }

            null -> binding.rbScheduled.isChecked = true
        }

        binding.switchWeather.isChecked = valve.isWeatherChecked
        binding.clWeather.background = ContextCompat.getDrawable(requireContext(),
            if (valve.isWeatherChecked) R.drawable.background_switch_selector
            else R.drawable.background_switch_selector_off
        )
        binding.switchActive.isChecked = valve.isActive
        binding.clActive.background = ContextCompat.getDrawable(requireContext(),
            if (valve.isActive) R.drawable.background_switch_selector
            else R.drawable.background_switch_selector_off
        )

        binding.etEveryNDays.setText((valve.schedule?.intervalDays ?: 2).toString())

        checkBoxes.forEach { (checkBox, day) ->
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = valve.schedule?.daysOfWeek?.contains(day) ?: false
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isBulkUpdating) return@setOnCheckedChangeListener
                if (isChecked) {
                    viewModel.addDayOfWeek(day)
                } else {
                    viewModel.removeDayOfWeek(day)
                    isBulkUpdating = true
                    binding.cbAll.isChecked = false
                    isBulkUpdating = false
                }
                updateAllCheck()
            }
        }
        updateAllCheck()

        adapter.cleanTimes()
        valve.schedule?.waterTimes?.forEach { time ->
            adapter.addTime(getTimeDescription(time))
        }

//        binding.etDuration.setText((valve.schedule?.duration ?: 3).toString())
        val durationSec = valve.schedule?.duration ?: MAX_DURATION_MINUTES

        if (durationSec >= 60) {
            durationType = DurationType.MINUTES
            binding.rbMin.isChecked = true

            val minutes = (durationSec / 60).coerceIn(MIN_DURATION_MINUTES, MAX_DURATION_MINUTES)

            isDurationInternalUpdate = true
            binding.etDuration.setText(minutes.toString())
            isDurationInternalUpdate = false

        } else {
            durationType = DurationType.SECONDS
            binding.rbSeg.isChecked = true

            val seconds = durationSec.coerceIn(1, 60)

            isDurationInternalUpdate = true
            binding.etDuration.setText(seconds.toString())
            isDurationInternalUpdate = false
        }


        val minHum = valve.humidityMin
        binding.sliderHumMin.value = minHum.toFloat()
        binding.tvHumMinVal.text = "$minHum%"

        val maxHum = valve.humidityMax
        binding.sliderHumMax.value = maxHum.toFloat()
        binding.tvHumMaxVal.text = "$maxHum%"
    }

    private fun setFrequencyView(frequencyMode: FrequencyMode?) {
        when (frequencyMode ?: FrequencyMode.INTERVAL_DAYS) {
            FrequencyMode.SELECTED_DAYS -> {
                binding.clEveryNDays.visibility = View.INVISIBLE
                binding.flowDaysOfWeek.visibility = View.VISIBLE
                binding.cbAll.visibility = View.VISIBLE
            }
            FrequencyMode.INTERVAL_DAYS -> {
                binding.clEveryNDays.visibility = View.VISIBLE
                binding.flowDaysOfWeek.visibility = View.INVISIBLE
                binding.cbAll.visibility = View.INVISIBLE
            }
        }
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
        private const val MAX_DAYS = 30
    }

}