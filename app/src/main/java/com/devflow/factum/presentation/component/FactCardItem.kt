package com.devflow.factum.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devflow.factum.R
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.ui.theme.FactumTheme
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size
import com.devflow.factum.util.Temp
import com.devflow.factum.util.VisualContent
import java.util.Locale

@Composable
fun FactCardItem(
    fact: Fact,
    icon: Int,
    color: Color,
    onClickAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(Size.RoundedCornerShapeBig))
            .clickable(
                role = Role.Button,
                onClick = onClickAction
            ),
        shape = RoundedCornerShape(Size.RoundedCornerShapeBig),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .height(Size.CardItemHeight)
                .padding(Padding.Standard),
            verticalArrangement = Arrangement.spacedBy(Padding.Standard)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(Size.ImageBig)
                )
                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(Size.RoundedCornerShapeExtremelyBig)
                        )
                        .padding(Padding.SmallExtra),
                    horizontalArrangement = Arrangement.spacedBy(Padding.SmallExtraExtraExtra),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_filled_time_24),
                        contentDescription = null,
                        modifier = Modifier.size(Size.ImageExtremelySmall),
                        tint = MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = String.format(
                            locale = Locale.getDefault(),
                            format = "%s %d %s",
                            stringResource(R.string.read),
                            fact.readTime,
                            stringResource(R.string.minute)
                        ),
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(Padding.Small)) {
                Text(
                    text = fact.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = fact.text,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
private fun FactCardItemPreview() {
    FactumTheme {
        val factIconList = VisualContent.getIcons()
        val factColorList = VisualContent.getCardColors()
        val categoryList = stringArrayResource(R.array.category_items)

//        Box(
//            modifier = Modifier.fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//                .padding(Padding.Standard),
//            contentAlignment = Alignment.Center
//        ) {
//            FactCardItem(
//                fact = Temp.getFact(),
//                icon = factIconList.first(),
//                color = factColorList.first(),
//                onClickAction = {
//
//                }
//            )
//        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(Padding.Standard),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(Temp.getFactList()) { index, fact ->
                val localIndex = categoryList.indexOf(fact.factBase.category)

                FactCardItem(
                    fact = fact,
                    icon = factIconList[localIndex],
                    color = factColorList[localIndex],
                    onClickAction = {

                    }
                )
            }
        }
    }
}