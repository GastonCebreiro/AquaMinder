package com.example.aquaminder.feature_new_irrigation_zone.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import java.util.UUID

@AndroidEntryPoint
class NewIrrigationZoneFragment : Fragment() {

    private val viewModel: NewIrrigationZoneViewModel by viewModels()

    private lateinit var binding: FragmentNewIrrigationZoneBinding

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var bluetoothPermissionLauncher: ActivityResultLauncher<Array<String>>

    private var bluetoothGatt: BluetoothGatt? = null
    private var wifiCharacteristic: android.bluetooth.BluetoothGattCharacteristic? = null

    private val WIFI_SERVICE_UUID =
        java.util.UUID.fromString("0000abcd-0000-1000-8000-00805f9b34fb")
    private val WIFI_CHARACTERISTIC_UUID =
        java.util.UUID.fromString("0000abce-0000-1000-8000-00805f9b34fb")

    private var wifiCheckHandler: Handler? = null
    private var wifiCheckRunnable: Runnable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewIrrigationZoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBluetooth()

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

    private fun setBluetooth() {
        bluetoothPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                val allGranted = permissions.entries.all { it.value }

                if (allGranted) {
                    if (!isBluetoothEnabled()) {
                        requestEnableBluetooth()
                        return@registerForActivityResult
                    }
                    startBleScan()
                } else {
                    showErrorMessage("Permisos de Bluetooth denegados")
                }
            }

        binding.btnWifi.setOnClickListener {
            checkBluetoothPermissions()
        }
    }

    private fun isBluetoothEnabled(): Boolean {
        val bluetoothManager =
            requireContext().getSystemService(BluetoothManager::class.java)
        val adapter = bluetoothManager.adapter
        return adapter?.isEnabled == true
    }

    private fun requestEnableBluetooth() {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        enableBluetoothLauncher.launch(intent)
    }

    private val enableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                startBleScan()
            } else {
                showErrorMessage("Bluetooth apagado. No se puede continuar.")
            }
        }

    private fun checkBluetoothPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isEmpty()) {
            if (!isBluetoothEnabled()) {
                requestEnableBluetooth()
                return
            }
            startBleScan()
        } else {
            bluetoothPermissionLauncher.launch(missing.toTypedArray())
        }
    }

    @SuppressLint("MissingPermission")
    private fun startBleScan() {
        DialogUtils.showWifiInfoDialog(requireContext()) {
            Toast.makeText(requireContext(), "Buscando dispositivo…", Toast.LENGTH_SHORT).show()

            val bluetoothAdapter =
                (requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager)
                    .adapter

            val scanner = bluetoothAdapter.bluetoothLeScanner

            scanner.startScan(scanCallback)
        }
    }

    private val scanCallback = object : android.bluetooth.le.ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: android.bluetooth.le.ScanResult) {
            val device = result.device

            Log.d("GASTON", "Encontrado: ${device.name} - ${device.address}")

            if (device.name == "ESP32-SETUP") {
                Toast.makeText(requireContext(), "Dispositivo detectado", Toast.LENGTH_SHORT).show()

                val scanner =
                    (requireContext().getSystemService(Context.BLUETOOTH_SERVICE)
                            as android.bluetooth.BluetoothManager)
                        .adapter.bluetoothLeScanner

                scanner.stopScan(this)

                connectToEsp32(device)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToEsp32(device: BluetoothDevice) {
        device.connectGatt(requireContext(), false, gattCallback)
    }

    private val gattCallback = object : android.bluetooth.BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.d("GASTON", "Conectado a ESP32")
                gatt.discoverServices()
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.d("GASTON", "Servicios descubiertos")

            bluetoothGatt = gatt

            val wifiService = gatt.getService(WIFI_SERVICE_UUID)

            if (wifiService == null) {
                Log.e("GASTON", "Servicio WiFi no encontrado")
                return
            }
            wifiCharacteristic = wifiService.getCharacteristic(WIFI_CHARACTERISTIC_UUID)

            Log.d("GASTON", "wifiCharacteristic LISTA")

            requireActivity().runOnUiThread {
                DialogUtils.showWifiCredentialsDialog(
                    context = requireContext(),
                    onSendAction = { ssid, password ->
                        sendWifiCredentials(ssid, password)
                    }
                )
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            if (characteristic.uuid == WIFI_CHARACTERISTIC_UUID) {
                val text = value.toString(Charsets.UTF_8)
                Log.d("GASTON", "READ (API33+) -> $text")
                handleWifiStatus(text)
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (characteristic.uuid == WIFI_CHARACTERISTIC_UUID) {
                val text = characteristic.value.toString(Charsets.UTF_8)
                Log.d("GASTON", "READ (LEGACY) -> $text")
                handleWifiStatus(text)
            }
        }

    }

    private fun handleWifiStatus(text: String) {
        requireActivity().runOnUiThread {
            when (text) {
                "WIFI_OK" -> {
                    Toast.makeText(requireContext(), "WiFi conectada ✔", Toast.LENGTH_LONG).show()
                    setWifiStatus(isConnected = true)
                    wifiCheckHandler?.removeCallbacks(wifiCheckRunnable!!)
                }

                "WIFI_FAIL" -> {
                    Toast.makeText(requireContext(), "Error al conectar ❌", Toast.LENGTH_LONG)
                        .show()
                    setWifiStatus(isConnected = false)
                    wifiCheckHandler?.removeCallbacks(wifiCheckRunnable!!)
                }
            }
        }
    }

    private fun setWifiStatus(isConnected: Boolean) {
        setWifiLoading(false)
        if (isConnected) {
            binding.btnWifi.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.green)
            )
            binding.btnWifi.isEnabled = false
            binding.btnWifi.text = getString(R.string.fragment_new_irrigation_connected)
            binding.btnWifi.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            binding.btnWifi.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.orange)
            )
            binding.btnWifi.isEnabled = true
            binding.btnWifi.text = getString(R.string.fragment_new_irrigation_retry)
            binding.btnWifi.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendWifiCredentials(ssid: String, password: String) {
        val data = "$ssid:$password"
        Log.d("GASTON", "Enviar: $data")
        wifiCharacteristic?.value = data.toByteArray(Charsets.UTF_8)

        val success = bluetoothGatt?.writeCharacteristic(wifiCharacteristic)

        if (success == false) {
            showErrorMessage("Error enviando credenciales al dispositivo")
            return
        }

        Toast.makeText(requireContext(), "Enviando datos...", Toast.LENGTH_SHORT).show()
        setWifiLoading(true)
        checkWifiStatus()

    }

    private fun setWifiLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnWifi.isEnabled = false
            binding.btnWifi.text = ""
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnWifi.isEnabled = true
        }
    }

    private fun checkWifiStatus() {
        wifiCheckHandler = Handler(Looper.getMainLooper())
        var attempts = 0
        val maxAttempts = 10  // 10 intentos → ~15s total

        wifiCheckRunnable = object : Runnable {
            @SuppressLint("MissingPermission")
            override fun run() {
                attempts++

                Log.d("GASTON", "Leyendo WiFi status (intento $attempts)")
                bluetoothGatt?.readCharacteristic(wifiCharacteristic)

                if (attempts < maxAttempts) {
                    wifiCheckHandler?.postDelayed(this, 1500)
                } else {
                    Log.d("GASTON", "Timeout esperando WIFI_OK / WIFI_FAIL")
                    Toast.makeText(
                        requireContext(),
                        "Sin respuesta del dispositivo",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        wifiCheckHandler?.postDelayed(wifiCheckRunnable!!, 3000)
    }


    private fun setLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getUserLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permisos de Geolocalizador denegados.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.clLocation.setOnClickListener {
            checkLocationPermissionAndFetch()
        }

    }

    private fun checkLocationPermissionAndFetch() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
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
                Log.d("GASTON", "lat=$lat")
                Log.d("GASTON", "lon=$lon")
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
            binding.etInputId.error =
                getString(R.string.fragment_new_irrigation_zone_copy_message_error)
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

    override fun onDestroyView() {
        super.onDestroyView()
        wifiCheckHandler?.removeCallbacks(wifiCheckRunnable ?: return)
    }

}