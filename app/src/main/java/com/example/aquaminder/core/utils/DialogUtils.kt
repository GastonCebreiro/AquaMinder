package com.example.aquaminder.core.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.DrawableRes
import com.example.aquaminder.databinding.DialogLogoutBinding
import com.example.aquaminder.databinding.DialogModifyHumidityBinding
import com.example.aquaminder.databinding.DialogWifiCredentialsBinding
import com.example.aquaminder.databinding.ErrorGenericDialogBinding
import com.example.aquaminder.feature_configuration.utils.ValveUtils.getHumidityDescription

object DialogUtils {

    fun showErrorDialog(
        context: Context,
        @DrawableRes imageId: Int? = null,
        titleText: String? = null,
        messageText: String? = null,
        acceptText: String? = null,
        cancelText: String? = null,
        onAcceptAction: (() -> Unit)? = null,
        onCancelAction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        val binding = ErrorGenericDialogBinding
            .inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(binding.root)

        imageId?.let { binding.ivLogo.setImageResource(it) }
        titleText?.takeIf { it.isNotBlank() }?.let {
            binding.tvTitle.text = it
        }
        messageText?.takeIf { it.isNotBlank() }?.let {
            binding.tvMessage.visibility = View.VISIBLE
            binding.tvMessage.text = it
        }
        acceptText?.takeIf { it.isNotBlank() }?.let {
            binding.btnAccept.text = it
        }
        binding.btnAccept.setOnClickListener {
            onAcceptAction?.invoke()
            dialog.dismiss()
        }
        cancelText?.takeIf { it.isNotBlank() }?.let {
            binding.tvCancel.visibility = View.VISIBLE
            binding.tvCancel.text = it
            binding.tvCancel.setOnClickListener {
                onCancelAction?.invoke()
                dialog.dismiss()
            }
        }
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val width =
            (context.resources.displayMetrics.widthPixels * 0.85).toInt() // 85% of screen width
        val height = WindowManager.LayoutParams.WRAP_CONTENT // or a fixed height like 600
        dialog.window?.setLayout(width, height)
    }


    fun showLogoutDialog(
        context: Context,
        onAcceptAction: (() -> Unit)? = null,
        onCancelAction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        val binding = DialogLogoutBinding
            .inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(binding.root)

        binding.btnAccept.setOnClickListener {
            onAcceptAction?.invoke()
            dialog.dismiss()
        }
        binding.tvCancel.setOnClickListener {
            onCancelAction?.invoke()
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun showWifiCredentialsDialog(
        context: Context,
        onSendAction: (ssid: String, password: String) -> Unit,
        onCancelAction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        val binding = DialogWifiCredentialsBinding
            .inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val ssid = binding.etSsid.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (ssid.isEmpty()) {
                binding.tilSsid.error = "Ingrese el SSID"
                return@setOnClickListener
            }

            binding.tilSsid.error = null
            binding.tilPassword.error = null

            onSendAction(ssid, pass)
            dialog.dismiss()
        }

        binding.tvCancel.setOnClickListener {
            onCancelAction?.invoke()
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    // TODO GC DELETE
    fun showModifyHumidityDialog(
        context: Context,
        humidity: Int,
        onAcceptAction: ((Int) -> Unit)
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        val binding = DialogModifyHumidityBinding
            .inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(binding.root)

        val humidityText = getHumidityDescription(humidity)
        binding.tvHumidity.text = humidityText

        var selectedHumidity = humidity

        binding.sliderHumidity.valueFrom = 0f
        binding.sliderHumidity.valueTo = 100f
        binding.sliderHumidity.stepSize = 1f
        binding.sliderHumidity.value = humidity.toFloat()

        binding.sliderHumidity.addOnChangeListener { _, value, _ ->
            binding.tvHumidity.text = getHumidityDescription(value.toInt())
            selectedHumidity = value.toInt()
        }

        binding.btnSave.setOnClickListener {
            onAcceptAction.invoke(selectedHumidity)
            dialog.dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


}