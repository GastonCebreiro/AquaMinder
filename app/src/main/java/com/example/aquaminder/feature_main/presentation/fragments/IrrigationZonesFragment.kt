package com.example.aquaminder.feature_main.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquaminder.databinding.FragmentIrrigationZonesBinding
import com.example.aquaminder.feature_login.utils.LoginState
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

    override fun onStart() {
        super.onStart()

        viewModel.getIrrigationZones()

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.isLoading.collect { isLoading ->
                    binding.progressBar.isVisible = isLoading
                }
            }

            launch {
                viewModel.irrigationZoneState.collect { irrigationZoneState ->
                    when (irrigationZoneState) {
                        is IrrigationZoneState.Success -> {
//                        val cardAdapter = CardAdapter(irrigationZoneState.cardList) { cardSelected ->
//                            navToPayCardFragment(cardSelected)
//                        }
//                        binding.rvCards.layoutManager = LinearLayoutManager(context)
//                        binding.rvCards.adapter = cardAdapter
                        }

                        is IrrigationZoneState.Error -> {
                            showMessage(irrigationZoneState.errorMsg)
                        }

                        is IrrigationZoneState.EmptyList -> {
                            showEmptyListWarning(irrigationZoneState.emptyListMsg)
                        }

                        is IrrigationZoneState.Idle -> {}
                    }
                    viewModel.updateViewState(IrrigationZoneState.Idle)
                }
            }
        }
    }

//    private fun navToPayCardFragment(cardSelected: CardDomainModel) {
//        val action = FragmentDirections.actionHomeFragmentToPayCardFragment(cardSelected)
//        findNavController().navigate(action)
//    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showEmptyListWarning(message: String) {
        binding.tvEmptyWarning.visibility = View.VISIBLE
        binding.tvEmptyWarning.text = message
    }


}