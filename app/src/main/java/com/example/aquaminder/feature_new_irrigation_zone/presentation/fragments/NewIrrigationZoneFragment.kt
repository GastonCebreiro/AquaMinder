package com.example.aquaminder.feature_new_irrigation_zone.presentation.fragments

import android.content.ClipboardManager
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputType
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
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.core.utils.IdentifierUtils
import com.example.aquaminder.databinding.FragmentNewIrrigationZoneBinding
import com.example.aquaminder.feature_new_irrigation_zone.presentation.adapter.LogoViewPagerAdapter
import com.example.aquaminder.feature_new_irrigation_zone.presentation.view_model.NewIrrigationZoneViewModel
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

        binding.btnGenerateId.setOnClickListener {
            viewModel.createUUID()
            viewModel.setVerification(false)
            setButtonGenerateId(false)
            setInputData(true)
        }

        binding.btnDelete.setOnClickListener {
            clearInputId()
            setButtonSaveText(R.string.fragment_new_irrigation_zone_btn_save)
            setButtonGenerateId(true)
        }

        binding.btnSave.setOnClickListener {
            saveButtonAction()
        }

        setLogoAdapter()

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

                            is NewIrrigationZoneState.CreatedId -> {
                                binding.etInputId.setText(irrigationZoneState.id)
                            }

                            is NewIrrigationZoneState.Idle -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setLogoAdapter() {

        val logoList = viewModel.getLogos()
        val colorList = viewModel.getColors()

        val adapter = LogoViewPagerAdapter(logoList, colorList)

        binding.vpLogo.adapter = adapter

        TabLayoutMediator(binding.tlIndicators, binding.vpLogo) { _, _ -> }.attach()
    }

    private fun setButtonGenerateId(isEnable: Boolean) {
        if (isEnable) {
            binding.btnGenerateId.isEnabled = true
            binding.etInputId.isEnabled = true
            binding.btnGenerateId.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray
                ), PorterDuff.Mode.SRC_IN
            )
            return
        }
        binding.btnGenerateId.isEnabled = false
        binding.etInputId.isEnabled = false
        binding.btnGenerateId.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.gray_delete
            ), PorterDuff.Mode.SRC_IN
        )

    }

    private fun clearInputId() {
        binding.etInputId.setText("")
        binding.etInputId.clearFocus()
        setInputData(false)
    }

    private fun setButtonSaveText(textId: Int) {
        binding.btnSave.text = getString(textId)
    }

    private fun setInputData(isVisible: Boolean) {
        binding.clData.isVisible = isVisible
        binding.etInputName.setText("")
        binding.vpLogo.currentItem = 0
    }

    private fun saveButtonAction() {
        viewModel.saveIrrigationZone(
            inputId = binding.etInputId.text.toString(),
            inputName = binding.etInputName.text.toString(),
            inputLogo = binding.vpLogo.currentItem,
            inputColor = binding.vpLogo.currentItem
        )
    }

    private fun setInputId() {
        binding.etInputId.isFocusable = false
        binding.etInputId.isFocusableInTouchMode = false
        binding.etInputId.isCursorVisible = false
        binding.etInputId.inputType = InputType.TYPE_NULL

        binding.etInputId.setOnLongClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val pasteData = clipboard.primaryClip?.getItemAt(0)?.text?.toString().orEmpty()

            if (IdentifierUtils.isValidUUID(pasteData)) {
                viewModel.setVerification(true)
                binding.etInputId.setText(pasteData)
                setButtonSaveText(R.string.fragment_new_irrigation_zone_btn_verify)
                setButtonGenerateId(false)
                setInputData(false)
                showMessage(getString(R.string.fragment_new_irrigation_zone_copy_message))
            } else {
                clearInputId()
                showMessage(getString(R.string.fragment_new_irrigation_zone_copy_message_error))
            }
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