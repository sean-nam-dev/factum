package com.devflow.factum.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.devflow.factum.util.Padding

@Composable
fun SettingsItemLabel(
    text: String,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Column {
        Text(
            text = text,
            modifier = Modifier.padding(bottom = Padding.Small),
            style = MaterialTheme.typography.titleLarge
        )
        content()
    }
}