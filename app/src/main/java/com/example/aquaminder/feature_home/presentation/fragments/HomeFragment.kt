package com.example.aquaminder.feature_home.presentation.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentHomeBinding
import com.example.aquaminder.databinding.ItemSpinnerDropdownBinding
import com.example.aquaminder.databinding.ItemSpinnerSelectedBinding
import com.example.aquaminder.feature_configuration.domain.model.LastWatersDomainModel
import com.example.aquaminder.feature_configuration.domain.model.NextWatersDomainModel
import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.example.aquaminder.feature_home.presentation.adapter.LastWatersAdapter
import com.example.aquaminder.feature_home.presentation.adapter.NextWatersAdapter
import com.example.aquaminder.feature_home.presentation.view_models.HomeViewModel
import com.example.aquaminder.feature_home.utils.GraphUtils
import com.example.aquaminder.feature_home.utils.HomeState
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    private val humidityCache = mutableMapOf<Int, LineData>()

    private lateinit var lastWatersAdapter: LastWatersAdapter
    private lateinit var nextWatersAdapter: NextWatersAdapter

    private var selectedValve: ValveDomainModel? = null

    private var wateringJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getIrrigationZoneDetails()

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.includeProgressBar.clProgressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.homeState.collect { homeState ->
                        when (homeState) {
                            is HomeState.Success -> {
                                showDetails(homeState.details)
                            }

                            is HomeState.Error -> {
                                showErrorMessage(homeState.errorMsg)
                            }

                            is HomeState.Idle -> {}
                        }
                    }
                }

                launch {
                    viewModel.wateringState.collect { isWatering ->
                        showWatering(isWatering)
                    }
                }
            }
        }
    }

    private fun showWatering(isWatering: Boolean) {
        if (isWatering) {
            startWateringTextAnimation()
            binding.ivCheck.visibility = View.GONE
            binding.wbWatering.visibility = View.VISIBLE
            binding.wbWatering.setBackgroundColor(Color.TRANSPARENT)
            binding.wbWatering.settings.loadWithOverviewMode = true
            binding.wbWatering.settings.useWideViewPort = true
            binding.wbWatering.loadUrl("file:///android_asset/watering.html")
            binding.tvStatus.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_status_watering)
            binding.tvStatus.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_blue_weather_end
                )
            )
        } else {
            stopWateringTextAnimation()
            setupStatus(selectedValve?.isActive ?: false)
        }
    }

    private fun startWateringTextAnimation() {
        stopWateringTextAnimation()
        val base = getString(R.string.fragment_home_valve_status_watering)
        val states = listOf(base, "$base.", "$base..", "$base...")

        wateringJob = viewLifecycleOwner.lifecycleScope.launch {
            var index = 0
            while (isActive) {
                binding.tvStatus.text = states[index]
                index = (index + 1) % states.size
                delay(500L)
            }
        }
    }

    private fun stopWateringTextAnimation() {
        wateringJob?.cancel()
    }

    private fun showDetails(details: IrrigationZoneDetailsDomainModel) {
        val streetAndNumber = "${details.address.street} ${details.address.number}"
        val city = "(${details.address.city})"

        binding.clHome.visibility = View.VISIBLE

        binding.tvTitle.text = details.name
        binding.ivLogo.setImageResource(details.logoId)
        binding.tvStreetAndNumber.text = streetAndNumber
        binding.tvCity.text = city

        setValveSelector(details.valves)

    }

    private fun setLastWatersAdapter(lastWaters: List<LastWatersDomainModel>?) {
        if (!lastWaters.isNullOrEmpty()) {
            binding.tvEmptyLast.visibility = View.GONE
            binding.rvLastWaters.visibility = View.VISIBLE
            lastWatersAdapter = LastWatersAdapter(lastWaters)
            binding.rvLastWaters.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvLastWaters.adapter = lastWatersAdapter
        } else {
            binding.tvEmptyLast.visibility = View.VISIBLE
            binding.rvLastWaters.visibility = View.GONE
        }
    }

    private fun setNextWatersAdapter(
        nextWaters: List<NextWatersDomainModel>?,
        controlMode: ControlMode?
    ) {
        if (controlMode != ControlMode.SCHEDULED) {
            binding.tvNextWaters.visibility = View.GONE
            binding.rvNextWaters.visibility = View.GONE
            binding.tvEmptyNext.visibility = View.GONE
            return
        }

        binding.tvNextWaters.visibility = View.VISIBLE

        if (!nextWaters.isNullOrEmpty()) {
            binding.tvEmptyNext.visibility = View.GONE
            binding.rvNextWaters.visibility = View.VISIBLE
            nextWatersAdapter = NextWatersAdapter(nextWaters)
            binding.rvNextWaters.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvNextWaters.adapter = nextWatersAdapter
        } else {
            binding.tvEmptyNext.visibility = View.VISIBLE
            binding.rvNextWaters.visibility = View.GONE
        }
    }

    private fun setValveSelector(valves: List<ValveDomainModel>) {
        if (valves.isEmpty()) {
            showEmptyValves()
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
                selectedValve = valves[position]
                selectedValve?.let {
                    viewModel.startPolling(it.id)
                    showValveInfo(it)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showEmptyValves() {
        binding.clEmptyValves.visibility = View.VISIBLE
        binding.clValveInfo.visibility = View.GONE
        binding.spValves.visibility = View.GONE
    }

    private fun showValveInfo(valve: ValveDomainModel) {
        setupStatus(valve.isActive)

        setupChart(valve.id, valve.lastHumidity, valve.controlMode)

        setLastWatersAdapter(valve.lastWaters)

        setNextWatersAdapter(valve.nextWaters, valve.controlMode)
    }

    private fun setupStatus(isActive: Boolean) {
        binding.ivCheck.visibility = View.VISIBLE
        binding.wbWatering.visibility = View.GONE
        if (isActive) {
            binding.tvStatus.text = getString(R.string.fragment_home_valve_status_active)
            binding.tvStatus.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_blue
                )
            )
            binding.tvStatus.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_status)
            binding.ivCheck.setImageResource(R.drawable.ic_check)
        } else {
            binding.tvStatus.text = getString(R.string.fragment_home_valve_status_inactive)
            binding.tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.tvStatus.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_status_off)
            binding.ivCheck.setImageResource(R.drawable.ic_warning)
        }
    }

    private fun setupChart(valveId: Int, lastHumidity: List<Int>, controlMode: ControlMode?) {
        if (controlMode != ControlMode.SENSOR) {
            binding.humidityChart.visibility = View.GONE
            binding.tvLastHumidity.visibility = View.GONE
            binding.tvEmptyHumidity.visibility = View.GONE
            return
        }

        binding.tvLastHumidity.visibility = View.VISIBLE
        binding.humidityChart.visibility = View.VISIBLE

        binding.tvEmptyHumidity.isVisible = lastHumidity.isEmpty()

        val cached = humidityCache[valveId]

        if (cached != null) {
            binding.humidityChart.data = cached
            applyChartConfig()
            binding.humidityChart.invalidate()

            binding.humidityChart.post {
                binding.humidityChart.moveViewToX(cached.getEntryCount().toFloat())
            }

            return
        }

        val entries = lastHumidity.mapIndexed { index, value ->
            Entry(index.toFloat(), value.toFloat())
        }

        val dataSet = LineDataSet(entries, "% Humedad").apply {
            lineWidth = 2f
            setDrawCircles(true)
            setDrawCircleHole(false)
            circleRadius = 4f
            setDrawValues(false)
            color = ContextCompat.getColor(requireContext(), R.color.light_blue)
            setCircleColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
            enableDashedLine(10f, 5f, 0f)
        }

        val lineData = LineData(dataSet)

        // guardar en cache
        humidityCache[valveId] = lineData

        binding.humidityChart.data = lineData

        // reusar config
        applyChartConfig()

        binding.humidityChart.invalidate()

        binding.humidityChart.post {
            binding.humidityChart.moveViewToX(entries.size.toFloat())
        }
    }

    private fun applyChartConfig() {
        binding.humidityChart.description.isEnabled = false

        // Eje Y
        binding.humidityChart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 100f
            granularity = 10f
            setLabelCount(11, true)
        }
        binding.humidityChart.axisRight.isEnabled = false

        // Labels eje X
        val labels = GraphUtils.getLast24HoursLabels()
        binding.humidityChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            labelCount = 24
            valueFormatter = IndexAxisValueFormatter(labels)
        }

        // Grid
        binding.humidityChart.axisLeft.gridColor =
            ContextCompat.getColor(requireContext(), R.color.light_gray)
        binding.humidityChart.axisLeft.gridLineWidth = 0.5f

        binding.humidityChart.xAxis.gridColor =
            ContextCompat.getColor(requireContext(), R.color.light_gray)
        binding.humidityChart.xAxis.gridLineWidth = 0.5f

        // Textos
        binding.humidityChart.xAxis.typeface = Typeface.DEFAULT_BOLD
        binding.humidityChart.axisLeft.typeface = Typeface.DEFAULT_BOLD

        // Scroll
        binding.humidityChart.setDragEnabled(true)
        binding.humidityChart.setScaleEnabled(false)
        binding.humidityChart.setVisibleXRangeMaximum(6f)
        binding.humidityChart.setVisibleXRangeMinimum(6f)
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

    override fun onStop() {
        super.onStop()
        viewModel.stopPolling()
    }

}