package com.example.aquaminder.core.utils

import android.content.Context
import android.content.Intent
import com.example.aquaminder.R

object IdentifierUtils {

    // TODO GC DEFINE VALID ID
    fun isValidID(id: String) = id.length == ID_LENGTH

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

    private const val ID_LENGTH = 6

}