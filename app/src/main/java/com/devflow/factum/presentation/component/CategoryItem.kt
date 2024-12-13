package com.devflow.factum.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.devflow.factum.R
import com.devflow.factum.ui.theme.FactumTheme
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size

@Composable
fun CategoryItem(
    title: String,
    iconId: Int,
    onClickAction: () -> Unit
) {
    Column(
        modifier = Modifier.size(Size.ItemSize)
            .clip(RoundedCornerShape(Size.RoundedCornerShapeVeryBig))
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)
            )
            .clickable(
                onClickLabel = title,
                role = Role.Button,
                onClick = onClickAction
            )
            .padding(Padding.Small),
        verticalArrangement = Arrangement.spacedBy(Padding.SmallExtraExtra)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Image(
            painter = painterResource(iconId),
            contentDescription = title,
            modifier = Modifier.weight(1f)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
private fun CategoryItemPrev() {
    FactumTheme {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CategoryItem(
                "Наука",
                R.drawable.img_2_nature
            ) {}
        }
    }
}