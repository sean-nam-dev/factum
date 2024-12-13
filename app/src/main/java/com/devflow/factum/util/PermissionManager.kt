package com.devflow.factum.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

class PermissionManager(
    private val appContext: Context
) {
    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(appContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    @Composable
    fun RequestPermission(
        permission: String,
        onResult: (Boolean) -> Unit
    ) {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            onResult(isGranted)
        }

        LaunchedEffect(permission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isPermissionGranted(permission)) {
                launcher.launch(permission)
            } else {
                onResult(true)
            }
        }
    }
}