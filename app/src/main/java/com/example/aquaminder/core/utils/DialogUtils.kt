package com.example.aquaminder.core.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.aquaminder.R
import com.example.aquaminder.databinding.ErrorGenericDialogBinding

object DialogUtils {

    fun showErrorDialog(
        context: Context,
        @DrawableRes imageId: Int? = null,
        titleText: String? = null,
        @StringRes messageTextId: Int? = null,
        @StringRes acceptTextId: Int? = null,
        @StringRes cancelTextId: Int? = null,
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
        titleText?.let { binding.tvTitle.text = titleText }
        messageTextId?.let {
            binding.tvMessage.visibility = View.VISIBLE
            binding.tvMessage.setText(it)
        }
        acceptTextId?.let { binding.btnAccept.setText(it) }
        binding.btnAccept.setOnClickListener {
            onAcceptAction?.invoke()
            dialog.dismiss()
        }
        cancelTextId?.let {
            binding.tvCancel.visibility = View.VISIBLE
            binding.tvCancel.setText(it)
            binding.tvCancel.setOnClickListener {
                onCancelAction?.invoke()
                dialog.dismiss()
            }
        }
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}