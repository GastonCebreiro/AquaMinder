package com.example.aquaminder.feature_configuration.presentation.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentConfigurationBinding
import com.example.aquaminder.databinding.ItemSpinnerDropdownBinding
import com.example.aquaminder.databinding.ItemSpinnerSelectedBinding
import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel
import com.example.aquaminder.feature_configuration.presentation.view_models.ConfigurationViewModel
import com.example.aquaminder.feature_configuration.utils.ConfigurationState
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getDurationDescription
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getHumidityDescription
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getIntervalHoursDescription
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getTimeDescription
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConfigurationFragment : Fragment() {

    private val viewModel: ConfigurationViewModel by viewModels()

    private lateinit var binding: FragmentConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getIrrigationZoneConfiguration()

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.includeProgressBar.clProgressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.configurationState.collect { configurationState ->
                        when (configurationState) {
                            is ConfigurationState.Success -> {
                                setInitialState(configurationState.configuration)
                            }

                            is ConfigurationState.Error -> {
                                showErrorMessage(configurationState.errorMsg)
                            }

                            is ConfigurationState.Idle -> {}

                            is ConfigurationState.ConfigModified -> {
                                setValveSelector(configurationState.configuration?.valves.orEmpty())
                                showSaveButton(configurationState.isModified)
                                viewModel.restartState()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setInitialState(config: IrrigationZoneConfigDomainModel) {
        binding.clConfiguration.visibility = View.VISIBLE
        setButton()
//        setSwitch(config.isCheckHumidityEnabled, config.isCheckWeatherEnabled)
        setValveSelector(config.valves)
    }

    private fun setButton() {
        binding.btnSave.setOnClickListener {
            viewModel.saveConfiguration()
        }
    }

//    private fun goToValveConfig(selectedValve: ValveDomainModel) {
//        val action = ConfigurationFragmentDirections
//            .actionConfigurationFragmentToValveConfigFragment(selectedValve)
//
//        findNavController().navigate(action)
//    }

//    private fun setSwitch(checkHumidityEnabled: Boolean, checkWeatherEnabled: Boolean) {
//
//        setCheckHumidityStatus(checkHumidityEnabled)
//        setCheckWeatherStatus(checkWeatherEnabled)
//
//        binding.switchSensor.setOnCheckedChangeListener { _, isChecked ->
//            viewModel.setCheckHumidity(isChecked)
//            viewModel.setSwitchSound(isChecked)
//            setCheckHumidityStatus(isChecked)
//        }
//
//        binding.switchWeather.setOnCheckedChangeListener { _, isChecked ->
//            viewModel.setCheckWeather(isChecked)
//            viewModel.setSwitchSound(isChecked)
//            setCheckWeatherStatus(isChecked)
//        }
//    }
//
//    private fun setCheckHumidityStatus(isChecked: Boolean) {
//        binding.switchSensor.isChecked = isChecked
//        setTextStatus(binding.tvSensorState, isChecked)
//        setTextColorStatus(binding.tvSensorState, isChecked)
//        setTextColorStatus(binding.tvSensor, isChecked)
//        setImageColorStatus(binding.ivSensor, isChecked)
//    }
//
//    private fun setCheckWeatherStatus(isChecked: Boolean) {
//        binding.switchWeather.isChecked = isChecked
//        setTextStatus(binding.tvWeatherState, isChecked)
//        setTextColorStatus(binding.tvWeatherState, isChecked)
//        setTextColorStatus(binding.tvWeather, isChecked)
//        setImageColorStatus(binding.ivWeather, isChecked)
//    }

    private fun setTextStatus(textView: TextView, isEnable: Boolean) {
        textView.text =
            if (isEnable)
                getString(R.string.fragment_configuration_switch_on)
            else
                getString(R.string.fragment_configuration_switch_off)
    }

    private fun setTextColorStatus(textView: TextView, isEnable: Boolean) {
        textView.setTextColor(
            if (isEnable)
                ContextCompat.getColor(requireContext(), R.color.gray)
            else
                ContextCompat.getColor(requireContext(), R.color.gray_delete)
        )
    }

    private fun setImageColorStatus(imageView: ImageView, isEnable: Boolean) {
        imageView.setColorFilter(
            if (isEnable)
                ContextCompat.getColor(requireContext(), R.color.light_blue)
            else
                ContextCompat.getColor(requireContext(), R.color.gray_delete),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun setValveSelector(valves: List<ValveDomainModel>) {
        if (valves.isEmpty()) {
            // TODO GC SHOW NEW VALVE
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

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
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
                val selectedValve = valves[position]
                showValveInfo(selectedValve)
                binding.clEdit.setOnClickListener {
//                    goToValveConfig(selectedValve)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showValveInfo(valve: ValveDomainModel) {
        if (valve.isActive) {
            binding.tvStatus.text = getString(R.string.fragment_home_valve_status_active)
            binding.tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
            binding.ivCheck.setImageResource(R.drawable.ic_check)
        } else {
            binding.tvStatus.text = getString(R.string.fragment_home_valve_status_inactive)
            binding.tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.ivCheck.setImageResource(R.drawable.ic_warning)
        }

//        binding.tvSelectedHumidity.text = getHumidityDescription(valve.humidity)
//        binding.tvStartHour.text = getTimeDescription(valve.schedule?.startTime)
//        binding.tvIntervalHours.text = getIntervalHoursDescription(valve.schedule?.intervalHours)
//        binding.tvDuration.text = getDurationDescription(valve.schedule?.duration)

//        setUpDialogs(valve)
    }


//    private fun setUpDialogs(valve: ValveDomainModel) {
//        binding.tvSelectedHumidityLabel.setOnClickListener{
//            DialogUtils.showModifyHumidityDialog(
//                context = requireContext(),
//                humidity = viewModel.getNewHumidity(valve),
//                onAcceptAction = { newHumidity ->
//                    viewModel.setNewHumidity(valve, newHumidity)
//                    binding.tvSelectedHumidity.text = getHumidityText(newHumidity)
//                }
//            )
//        }
//    }

    private fun showSaveButton(isVisible: Boolean) {
        binding.btnSave.isEnabled = isVisible
    }

    private fun showErrorMessage(message: String, logoId: Int? = null) {
        DialogUtils.showErrorDialog(
            context = requireContext(),
            imageId = logoId,
            titleText = message,
            onAcceptAction = {
                goBack()
            }
        )
    }

    private fun goBack() {
        requireActivity().finish()
    }

}