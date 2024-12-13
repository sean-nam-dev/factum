package com.devflow.factum.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.devflow.factum.R
import com.devflow.factum.ui.theme.FactumTheme
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size

@Composable
fun SettingsOptionItem(
    title: String,
    iconId: Int? = null,
    iconColor: Color? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Size.RowHeight)
            .clip(RoundedCornerShape(Size.RoundedCornerShape))
            .clickable(
                role = Role.Button,
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.spacedBy(Padding.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconId != null && iconColor != null) {
            Icon(
                imageVector = ImageVector.vectorResource(iconId),
                contentDescription = null,
                modifier = Modifier.size(Size.IconSize),
                tint = iconColor
            )
        } else {
            Spacer(modifier = Modifier.size(Size.IconSize))
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun SettingsOptionItemPreview() {
    FactumTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(Padding.Standard)
        ) {
            SettingsOptionItem(
                title = "Уведомления",
                iconId = R.drawable.ic_round_settings_24,
                iconColor = MaterialTheme.colorScheme.primary,
                onClick = {}
            )
            SettingsOptionItem(
                title = "Уведомления",
                onClick = {}
            )
        }
    }
}