package com.example.aquaminder.feature_irrigation_zone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquaminder.databinding.FragmentIrrigationZoneDetailBinding
import com.example.aquaminder.databinding.FragmentIrrigationZonesBinding
import com.example.aquaminder.feature_login.utils.LoginState
import com.example.aquaminder.feature_main.presentation.adapter.IrrigationZoneAdapter
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_main.presentation.view_model.IrrigationZonesViewModel
import com.example.aquaminder.feature_main.utils.IrrigationZoneState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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