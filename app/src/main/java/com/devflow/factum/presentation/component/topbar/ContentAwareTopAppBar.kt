package com.devflow.factum.presentation.component.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.devflow.factum.R
import com.devflow.factum.navigation.Navigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentAwareTopAppBar(
    navigator: Navigator,
    backStackEntry:  NavBackStackEntry?
) {
    val routes = listOf(
        "com.devflow.factum.navigation.Destination.DetailScreen/{index}/{localIndex}",
        "com.devflow.factum.navigation.Destination.CategoryDetailScreen/{index}/{localIndex}/{category}",
        "com.devflow.factum.navigation.Destination.FavoriteDetailScreen/{index}/{localIndex}",
        "com.devflow.factum.navigation.Destination.CategoryScreen/{documentName}",
        "com.devflow.factum.navigation.Destination.FavoriteCategoriesScreen",
        "com.devflow.factum.navigation.Destination.NotificationScreen"
    )

    if (backStackEntry?.destination?.route in routes) {
        backStackEntry.let { entry ->
            val scope = rememberCoroutineScope()
            val viewModel: TopAppBarViewModel = viewModel(
                viewModelStoreOwner = entry!!,
                initializer = { TopAppBarViewModel() }
            )
            val color = viewModel.color ?: MaterialTheme.colorScheme.background

            TopAppBar(
                title = {
                    Text(
                        text = viewModel.headline,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch { navigator.navigateUp() }
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_round_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    viewModel.actions.forEach { item ->
                        IconButton(
                            onClick = item.action
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(item.iconId),
                                contentDescription = null,
                                tint = item.color
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = color,
                    scrolledContainerColor = color,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = viewModel.scrollBehavior
            )
        }
    }
}