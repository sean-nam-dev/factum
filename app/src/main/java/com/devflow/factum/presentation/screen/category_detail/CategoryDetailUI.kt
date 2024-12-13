package com.devflow.factum.presentation.screen.category_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import com.devflow.factum.R
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.presentation.component.topbar.AppBarActionItem
import com.devflow.factum.presentation.component.topbar.util.ProvideAppBarActions
import com.devflow.factum.presentation.component.topbar.util.ProvideAppBarColor
import com.devflow.factum.presentation.component.topbar.util.ProvideAppBarHeadline
import com.devflow.factum.presentation.component.topbar.util.ProvideScrollBehavior
import com.devflow.factum.presentation.screen.category.CategoryUIAction
import com.devflow.factum.util.Padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailUI(
    fact: Fact,
    color: Color,
    isFavorite: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onAction: (CategoryUIAction) -> Unit
) {
    val context = LocalContext.current

    val headLineIndex = stringArrayResource(R.array.category_items).indexOf(fact.factBase.category)
    val headLine = stringArrayResource(R.array.categories)[headLineIndex]

    ProvideScrollBehavior(scrollBehavior = scrollBehavior)
    ProvideAppBarHeadline(headline = "$headLine #${fact.factBase.documentId}")
    ProvideAppBarColor(color = color)
    ProvideAppBarActions(
        list = listOf(
            AppBarActionItem(
                iconId = R.drawable.ic_round_report_24,
                color = MaterialTheme.colorScheme.error,
                action = {
                    onAction(CategoryUIAction.OnReportAction(context, fact.factBase))
                }
            ),
            AppBarActionItem(
                iconId = if (isFavorite) R.drawable.ic_round_favorite_24
                else R.drawable.ic_outline_favorite_24,
                color = MaterialTheme.colorScheme.primary,
                action = {
                    onAction(CategoryUIAction.OnFavoriteItemAction(fact))
                }
            )
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(color)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(Padding.Standard),
        verticalArrangement = Arrangement.spacedBy(Padding.Standard)
    ) {
        item {
            Text(
                text = fact.title,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item {
            Text(
                text = fact.text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}