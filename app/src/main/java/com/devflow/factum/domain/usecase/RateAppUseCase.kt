package com.devflow.factum.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

class RateAppUseCase @Inject constructor(

) {
    fun execute(context: Context) {
        val googlePlayIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(GOOGLE_PLAY_URI + TEMP)
            setPackage(GOOGLE_PLAY_PACKAGE_NAME)
        }
        val webIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(GOOGLE_PLAY_WEB_URI + TEMP)
        }

        try {
            context.startActivity(googlePlayIntent)
        } catch (e: Exception) {
            context.startActivity(webIntent)
        }
    }

    companion object {
        private const val GOOGLE_PLAY_URI = "market://details?id="
        private const val GOOGLE_PLAY_WEB_URI = "https://play.google.com/store/apps/details?id="
        private const val GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending"
        private const val TEMP = "com.tencent.ig"
    }
}