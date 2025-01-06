package com.devflow.factum.presentation.screen.notification

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devflow.factum.R
import com.devflow.factum.domain.model.Time
import com.devflow.factum.presentation.component.TimeItem
import com.devflow.factum.presentation.component.TimePickerAlertDialog
import com.devflow.factum.presentation.component.topbar.AppBarActionItem
import com.devflow.factum.presentation.component.topbar.util.ProvideAppBarActions
import com.devflow.factum.presentation.component.topbar.util.ProvideAppBarHeadline
import com.devflow.factum.presentation.screen.settings.SettingsUIAction
import com.devflow.factum.presentation.screen.settings.SettingsUIState
import com.devflow.factum.ui.theme.FactumTheme
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("InlinedApi")
@Composable
fun NotificationUI(
    state: SettingsUIState,
    onAction: (SettingsUIAction) -> Unit
) {
    val headLine = stringResource(id = R.string.notifications)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onAction(SettingsUIAction.OnCheckPermission)
    }

    val currentTime = Calendar.getInstance()
    val timeInputState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    ProvideAppBarHeadline(headline = headLine)
    if (state.hasNotificationPermission) {
        ProvideAppBarActions(
            list = listOf(
                AppBarActionItem(
                    iconId = R.drawable.ic_round_add_24,
                    color = MaterialTheme.colorScheme.primary,
                    action = {
                        onAction(SettingsUIAction.OnAddNewTimeSlot)
                    }
                )
            )
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Padding.Standard),
        contentPadding = PaddingValues(Padding.Standard)
    ) {
        if (state.hasNotificationPermission) {
            items(state.timeList) { time ->
                TimeItem(
                    time = time,
                    confirmValueAction = { /*TODO*/ },
                    onCheckedChange = {}
                )
            }
        } else {
            item {
                Button(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.request_permission),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (state.isTimeDialogShown) {
            item {
                TimePickerAlertDialog(
                    state = timeInputState,
                    onDismissRequest = {
                        onAction(SettingsUIAction.OnTimeDialogDismissRequest)
                    },
                    onConfirmRequest = {
                        onAction(
                            SettingsUIAction.OnTimeDialogConfirmRequest(
                                Time(
                                    hour = timeInputState.hour,
                                    minute = timeInputState.minute
                                )
                            )
                        )
                    }
                )
            }
        }

        if (state.isConfirmationDialogShown) {

        }
    }
}

@Preview
@Composable
private fun NotificationUIPreview() {
    FactumTheme {
        var isShown by remember { mutableStateOf(true) }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
            NotificationUI(
                state = SettingsUIState(
                    categorySet = emptySet(),
                    hasNotificationPermission = true,
                    isTimeDialogShown = isShown
                ),
                onAction = {
                    isShown = false
                }
            )
        }
    }
}