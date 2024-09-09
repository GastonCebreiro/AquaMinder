package com.example.aquaminder.core.utils

import android.content.Context
import android.content.Intent
import com.example.aquaminder.R
import java.lang.reflect.Executable
import java.util.UUID

object IdentifierUtils {

    fun createUUID(): String = UUID.randomUUID().toString().substring(0, 28)

    fun shareId(context: Context, id: String) {
        try {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    id
                )
                type = "text/plain"
            }
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.fragment_irrigation_zones_share_id_label)
                )
            )
        } catch (e: Exception) {
            DialogUtils.showErrorDialog(context)
        }
    }

}