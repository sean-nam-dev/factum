package com.devflow.factum.domain.usecase

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.devflow.factum.R
import com.devflow.factum.util.ContextResourceManager
import javax.inject.Inject

class ContactThroughMailUseCase @Inject constructor(
    private val resourceManager: ContextResourceManager
){
    suspend fun execute(
        context: Context,
        subject: String,
        onFailAction: suspend () -> Unit
    ) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(URI_STRING)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(GMAIL_ADDRESS))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        try {
            startActivity(
                context,
                Intent.createChooser(emailIntent, resourceManager.getString(R.string.choose_app_to_send_email)),
                null
            )
        } catch (e: ActivityNotFoundException) {
            onFailAction()
        }
    }

    companion object {
        const val URI_STRING = "mailto:"
        const val GMAIL_ADDRESS = "devflow.team@gmail.com"
    }
}