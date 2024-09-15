package com.example.aquaminder.feature_new_irrigation_zone.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.aquaminder.databinding.FragmentIrrigationZoneDetailBinding
import com.example.aquaminder.feature_main.presentation.view_model.IrrigationZonesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IrrigationZoneDetailFragment : Fragment() {

    private val viewModel: IrrigationZonesViewModel by viewModels()

    private lateinit var binding: FragmentIrrigationZoneDetailBinding

    private val args: IrrigationZoneDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIrrigationZoneDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = args.itemSelected.name

        viewModel.getIrrigationZones()

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

//                launch {
//                    viewModel.isLoading.collect { isLoading ->
//                        binding.progressBar.isVisible = isLoading
//                    }
//                }
//
//                launch {
//                    viewModel.irrigationZoneState.collect { irrigationZoneState ->
//                        when (irrigationZoneState) {
//                            is IrrigationZoneState.Success -> {
//                                val izAdapter = IrrigationZoneAdapter(
//                                    irrigationZoneList = irrigationZoneState.itemList,
//                                    onClick = {
//                                        navToIrrigationZoneDetail(it)
//                                    }
//                                )
////                        val cardAdapter = CardAdapter(irrigationZoneState.cardList) { cardSelected ->
////                            navToPayCardFragment(cardSelected)
////                        }
////                        binding.rvCards.layoutManager = LinearLayoutManager(context)
////                        binding.rvCards.adapter = cardAdapter
//                            }
//
//                            is IrrigationZoneState.Error -> {
//                                showMessage(irrigationZoneState.errorMsg)
//                            }
//
//                            is IrrigationZoneState.EmptyList -> {
//                                showEmptyListWarning(irrigationZoneState.emptyListMsg)
//                            }
//
//                            is IrrigationZoneState.Idle -> {}
//                        }
//                    }
//                }
            }
        }
    }

//    private fun navToIrrigationZoneDetail(itemSelected: IrrigationZoneDomainModel) {
//        val action = FragmentDirections.actionHomeFragmentToPayCardFragment(cardSelected)
//        findNavController().navigate(action)
//    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

//    private fun showEmptyListWarning(message: String) {
//        binding.tvEmptyWarning.visibility = View.VISIBLE
//        binding.tvEmptyWarning.text = message
//    }


}