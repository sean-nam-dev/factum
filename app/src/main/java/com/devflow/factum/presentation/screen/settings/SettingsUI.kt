package com.devflow.factum.presentation.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.devflow.factum.R
import com.devflow.factum.presentation.component.SettingsItemLabel
import com.devflow.factum.presentation.component.SettingsOptionItem
import com.devflow.factum.util.Padding
import com.devflow.factum.util.VisualContent

@Composable
fun SettingsUI(
    onAction: (SettingsUIAction) -> Unit
) {
    val settingsPersonalizationTitleList = stringArrayResource(R.array.settings_personalization)
    val settingsPersonalizationIconList = VisualContent.getSettingsPersonalizationIconList()

    val settingsOtherTitleList = stringArrayResource(R.array.settings_other)
    val settingsOtherIconList = VisualContent.getSettingsOtherIconList()

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Padding.Standard),
        verticalArrangement = Arrangement.spacedBy(Padding.Large)
    ) {
        item {
            SettingsItemLabel(
                text = stringResource(R.string.personalization)
            ) {
                settingsPersonalizationTitleList.forEachIndexed { index, title ->
                    SettingsOptionItem(
                        title = title,
                        iconId = settingsPersonalizationIconList[index],
                        iconColor = MaterialTheme.colorScheme.surface,
                        onClick = { onAction(SettingsUIAction.OnPersonalizationItemClick(index)) }
                    )
                }
            }
        }
        item {
            SettingsItemLabel(
                text = stringResource(R.string.other)
            ) {
                settingsOtherTitleList.forEachIndexed { index, title ->
                    SettingsOptionItem(
                        title = title,
                        iconId = settingsOtherIconList[index],
                        iconColor = MaterialTheme.colorScheme.surface,
                        onClick = { onAction(SettingsUIAction.OnOtherItemClick(index, context)) }
                    )
                }
            }
        }
    }
}