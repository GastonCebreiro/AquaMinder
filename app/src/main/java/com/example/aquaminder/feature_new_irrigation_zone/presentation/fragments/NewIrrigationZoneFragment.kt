package com.example.aquaminder.feature_new_irrigation_zone.presentation.fragments

import android.content.ClipboardManager
import android.content.Context
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
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentNewIrrigationZoneBinding
import com.example.aquaminder.feature_new_irrigation_zone.presentation.adapter.LogoViewPagerAdapter
import com.example.aquaminder.feature_new_irrigation_zone.presentation.view_model.NewIrrigationZoneViewModel
import com.example.aquaminder.feature_new_irrigation_zone.utils.IrrigationZoneUtils.getColors
import com.example.aquaminder.feature_new_irrigation_zone.utils.IrrigationZoneUtils.getLogos
import com.example.aquaminder.feature_new_irrigation_zone.utils.NewIrrigationZoneState
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewIrrigationZoneFragment : Fragment() {

    private val viewModel: NewIrrigationZoneViewModel by viewModels()

    private lateinit var binding: FragmentNewIrrigationZoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewIrrigationZoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDelete.setOnClickListener {
            clearInputId()
        }

        binding.btnSave.setOnClickListener {
            saveButtonAction()
        }

        setLogoAdapter()
        setFocusListener()
        setInputId()

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
                            is NewIrrigationZoneState.Success -> {
                                showMessage(irrigationZoneState.message)
                                navToIrrigationZones()
                            }

                            is NewIrrigationZoneState.Error -> {
                                showErrorMessage(irrigationZoneState.errorMsg)
                            }

                            is NewIrrigationZoneState.ValidID -> {
                                binding.etInputId.setText(irrigationZoneState.id)
                            }

                            is NewIrrigationZoneState.InvalidID -> {
                                clearInputId()
                                setErrorID(true)
                            }

                            is NewIrrigationZoneState.Idle -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setFocusListener() {
        binding.etInputId.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.btnDelete.visibility = View.VISIBLE
                setErrorID(false)
            }
        }

        binding.etInputName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val inputID = binding.etInputId.text.toString()
                viewModel.checkValidID(id = inputID)
            }
        }
    }

    private fun setLogoAdapter() {

        val logoList = getLogos()
        val colorList = getColors()

        val adapter = LogoViewPagerAdapter(logoList, colorList)

        binding.vpLogo.adapter = adapter

        TabLayoutMediator(binding.tlIndicators, binding.vpLogo) { _, _ -> }.attach()
    }

    private fun clearInputId() {
        binding.etInputId.setText("")
        binding.etInputId.clearFocus()
        binding.btnDelete.visibility = View.GONE
    }

    private fun setErrorID(isError: Boolean) {
        if (isError)
            binding.etInputId.error = getString(R.string.fragment_new_irrigation_zone_copy_message_error)
        else
            binding.etInputId.error = null
    }

    private fun saveButtonAction() {
        viewModel.saveIrrigationZone(
            inputId = binding.etInputId.text.toString(),
            inputName = binding.etInputName.text.toString(),
            inputLogo = binding.vpLogo.currentItem
        )
    }

    private fun setInputId() {
        binding.etInputId.setOnLongClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val pasteData = clipboard.primaryClip?.getItemAt(0)?.text?.toString().orEmpty()

            binding.etInputId.setText(pasteData)
            showMessage(getString(R.string.fragment_new_irrigation_zone_copy_message))

            viewModel.checkValidID(id = pasteData)
            true
        }
    }

    private fun navToIrrigationZones() {
        findNavController().navigateUp()
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


}