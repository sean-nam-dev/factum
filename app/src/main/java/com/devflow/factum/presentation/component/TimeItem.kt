package com.devflow.factum.presentation.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devflow.factum.domain.model.Time
import com.devflow.factum.ui.theme.FactumTheme
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size
import com.devflow.factum.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeItem(
    time: Time,
    enabled: Boolean,
    deletionRequest: (Time) -> Unit,
    onCheckedChange: (Time) -> Unit
) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            deletionRequest(time)
            false
        },
        positionalThreshold = {
            150f
        }
    )

    SwipeToDismissBox(
        state = state,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Padding.SmallExtraExtraExtraExtra)
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(Size.RoundedCornerShapeVeryBig)
                    ),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_delete_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(Padding.Standard))
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(Size.RoundedCornerShapeVeryBig)
                )
                .padding(
                    horizontal = Padding.Small,
                    vertical = Padding.SmallLight
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${time.hour}:${time.minute}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )
            Switch(
                checked = time.isActive,
                onCheckedChange = {
                    onCheckedChange(time)
                },
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onBackground,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedBorderColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface,
                    uncheckedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview
@Composable
private fun TimeItemPreview() {
    var isConfirmed by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isConfirmed) {
        Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
    }

    FactumTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)) {
            TimeItem(
                time = Time(
                    hour = 15,
                    minute = 59,
                    isActive = true
                ),
                enabled = false,
                onCheckedChange = {},
                deletionRequest = { isConfirmed = !isConfirmed }
            )
            TimeItem(
                time = Time(
                    hour = 3,
                    minute = 27,
                    isActive = false
                ),
                enabled = true,
                onCheckedChange = {},
                deletionRequest = { isConfirmed = !isConfirmed }
            )
            TimeItem(
                time = Time(
                    hour = 11,
                    minute = 34,
                    isActive = true
                ),
                enabled = true,
                onCheckedChange = {},
                deletionRequest = { isConfirmed = !isConfirmed }
            )
        }
    }
}