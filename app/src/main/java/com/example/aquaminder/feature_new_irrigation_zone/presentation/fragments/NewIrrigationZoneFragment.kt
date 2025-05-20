package com.example.aquaminder.feature_new_irrigation_zone.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.aquaminder.databinding.FragmentNewIrrigationZoneBinding
import com.example.aquaminder.feature_main.domain.model.toSingleString
import com.example.aquaminder.feature_main.utils.AddressUtils.getAddressModelByCoordinates
import com.example.aquaminder.feature_new_irrigation_zone.presentation.adapter.LogoViewPagerAdapter
import com.example.aquaminder.feature_new_irrigation_zone.presentation.view_model.NewIrrigationZoneViewModel
import com.example.aquaminder.feature_new_irrigation_zone.utils.IrrigationZoneUtils.getColors
import com.example.aquaminder.feature_new_irrigation_zone.utils.IrrigationZoneUtils.getLogos
import com.example.aquaminder.feature_new_irrigation_zone.utils.NewIrrigationZoneState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class NewIrrigationZoneFragment : Fragment() {

    private val viewModel: NewIrrigationZoneViewModel by viewModels()

    private lateinit var binding: FragmentNewIrrigationZoneBinding

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        setLocation()

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.includeProgressBar.clProgressBar.isVisible = isLoading
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

    private fun setLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getUserLocation()
            } else {
                Toast.makeText(requireContext(), "Permisos de Geolocalizador denegados.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.clLocation.setOnClickListener {
            checkLocationPermissionAndFetch()
        }

    }

    private fun checkLocationPermissionAndFetch() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
                getUserLocation()
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val lat = location.latitude
                val lon = location.longitude
                Log.d("GASTON","lat=$lat")
                Log.d("GASTON","lon=$lon")
                getAddressFromCoordinates(lat, lon)
            } ?: run {
                showErrorMessage(getString(R.string.fragment_new_irrigation_zone_location_not_found_error))
            }
        }
    }

    private fun getAddressFromCoordinates(lat: Double, lon: Double) {
        val addressModel = getAddressModelByCoordinates(requireContext(), lat, lon)
        addressModel?.let {
            viewModel.saveLocation(lat, lon, it)
            binding.tvLocation.text = it.toSingleString()
        } ?: run {
            showErrorMessage(getString(R.string.fragment_new_irrigation_zone_location_not_found_error))
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