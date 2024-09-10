package com.example.aquaminder.core.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.aquaminder.R
import com.example.aquaminder.databinding.DialogLogoutBinding
import com.example.aquaminder.databinding.ErrorGenericDialogBinding

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

}