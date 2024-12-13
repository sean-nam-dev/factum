package com.devflow.factum.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.devflow.factum.R
import com.devflow.factum.ui.theme.FactumTheme
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size

@Composable
fun CategoryPickerItem(
    imageId: Int,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(Size.RowHeight)
            .clip(RoundedCornerShape(Size.RoundedCornerShape))
            .toggleable(
                value = isChecked,
                onValueChange = onCheckedChange
            ),
        horizontalArrangement = Arrangement.spacedBy(Padding.Standard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = null,
            modifier = Modifier.size(Size.Image)
                .clip(CircleShape)
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                uncheckedColor = MaterialTheme.colorScheme.onBackground,
                checkmarkColor = MaterialTheme.colorScheme.background
            )
        )
    }
}

@Preview
@Composable
private fun CategoryPickerItemPreview() {
    val (checkedState, onStateChange) = remember { mutableStateOf(true) }

    FactumTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Padding.Standard, vertical = Padding.Large),
        ) {
            item {
                CategoryPickerItem(
                    imageId = R.drawable.img_1_psycology,
                    title = "Психология",
                    isChecked = checkedState,
                    onCheckedChange = { onStateChange(!checkedState) }
                )
            }
            item {
                CategoryPickerItem(
                    imageId = R.drawable.img_1_kpop,
                    title = "Кпоп",
                    isChecked = !checkedState,
                    onCheckedChange = { onStateChange(!checkedState) }
                )
            }
        }
    }
}