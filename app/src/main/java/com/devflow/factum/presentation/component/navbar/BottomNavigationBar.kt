package com.devflow.factum.presentation.component.navbar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.Navigator
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    navigator: Navigator
) {
    val navRoutes = listOf(
        Destination.HomeScreen.toString(),
        Destination.CategoriesScreen.toString(),
        Destination.FavoriteScreen.toString(),
        Destination.SettingsScreen.toString()
    )
    val scope = rememberCoroutineScope()

    if (currentRoute in navRoutes) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background
        ) {
            NavItems.entries.forEach { item ->
                val isCurrent = currentRoute == item.route.toString()
                val icon = if (isCurrent) item.activeIconId else item.passiveIconId

                NavigationBarItem(
                    selected = isCurrent,
                    onClick = {
                        if (!isCurrent) {
                            scope.launch {
                                navigator.navigate(
                                    destination = item.route,
                                    navOptions = {
                                        popUpTo(route = Destination.HomeScreen) {
                                            inclusive = false
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                )
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = item.name
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(item.titleId),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isCurrent) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                        unselectedIconColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}

