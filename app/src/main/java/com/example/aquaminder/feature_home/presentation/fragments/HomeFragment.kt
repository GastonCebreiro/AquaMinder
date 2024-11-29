package com.example.aquaminder.feature_home.presentation.fragments

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
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentHomeBinding
import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel
import com.example.aquaminder.feature_home.presentation.view_models.HomeViewModel
import com.example.aquaminder.feature_home.utils.HomeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

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
                        binding.clProgressBar.isVisible = isLoading
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
            }
        }
    }

    private fun showDetails(details: IrrigationZoneDetailsDomainModel) {
        binding.tvTitle.text = details.name
        binding.ivLogo.setImageResource(details.logoId)
    }

//    private fun navToIrrigationZoneDetail(itemSelected: IrrigationZoneDomainModel) {
//        val action = FragmentDirections.actionHomeFragmentToPayCardFragment(cardSelected)
//        findNavController().navigate(action)
//    }

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