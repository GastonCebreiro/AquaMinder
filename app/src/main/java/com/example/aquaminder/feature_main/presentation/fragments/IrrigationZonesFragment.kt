package com.example.aquaminder.feature_main.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AdapterUtils
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.core.utils.IdentifierUtils
import com.example.aquaminder.databinding.FragmentIrrigationZonesBinding
import com.example.aquaminder.feature_main.presentation.adapter.IrrigationZoneAdapter
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_main.presentation.view_model.IrrigationZonesViewModel
import com.example.aquaminder.feature_main.utils.IrrigationZoneState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IrrigationZonesFragment : Fragment() {

    private val viewModel: IrrigationZonesViewModel by viewModels()

    private lateinit var binding: FragmentIrrigationZonesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIrrigationZonesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabNewIZ.setOnClickListener {
            navToNewIrrigationZone()
        }

        viewModel.getIrrigationZones()

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.irrigationZoneState.collect { irrigationZoneState ->
                        when (irrigationZoneState) {
                            is IrrigationZoneState.Success -> {
                                val izList = irrigationZoneState.itemList.toMutableList()
                                val izAdapter = IrrigationZoneAdapter(
                                    irrigationZoneList = izList,
                                    onClick = {
                                        navToIrrigationZoneDetail(it)
                                    },
                                    onShareId = { id ->
                                        if (id.isBlank()) {
                                            showErrorMessage(
                                                resources.getString(
                                                    R.string.fragment_irrigation_zones_share_id_error
                                                )
                                            )
                                            return@IrrigationZoneAdapter
                                        }
                                        shareIrrigationZoneId(id)
                                    }
                                )
                                binding.rvIrrigationZones.layoutManager =
                                    LinearLayoutManager(context)
                                binding.rvIrrigationZones.adapter = izAdapter
                                setUpSweepDelete(izAdapter)
                            }

                            is IrrigationZoneState.Error -> {
                                showErrorMessage(irrigationZoneState.errorMsg)
                            }

                            is IrrigationZoneState.EmptyList -> {
                                showEmptyListWarning(irrigationZoneState.emptyListMsg)
                            }

                            is IrrigationZoneState.Idle -> {}
                        }
                    }
                }
            }
        }
    }
    private fun setUpSweepDelete(izAdapter: IrrigationZoneAdapter) {
        AdapterUtils.setUpItemTouchHelper(binding.rvIrrigationZones, izAdapter) { position ->
            izAdapter.removeItem(requireContext(), position)
            // TODO GC SEND DELETED ITEM TO BE
        }
    }

    private fun shareIrrigationZoneId(id: String) {
        IdentifierUtils.shareId(requireContext(), id)
    }

    private fun navToNewIrrigationZone() {
        val action = IrrigationZonesFragmentDirections.actionIrrigationZonesFragmentToNewIrrigationZoneFragment()
        findNavController().navigate(action)
    }


    private fun navToIrrigationZoneDetail(itemSelected: IrrigationZoneDomainModel) {
        val action =
            IrrigationZonesFragmentDirections.actionIrrigationZonesFragmentToIrrigationZoneDetailFragment(
                itemSelected
            )
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessage(message: String, logoId: Int? = null) {
        DialogUtils.showErrorDialog(
            context = requireContext(),
            imageId = logoId,
            titleText = message,
        )
    }

    private fun showEmptyListWarning(message: String) {
        binding.tvEmptyWarning.visibility = View.VISIBLE
        binding.ivEmptyWarning.visibility = View.VISIBLE
        binding.tvEmptyWarning.text = message
    }


}