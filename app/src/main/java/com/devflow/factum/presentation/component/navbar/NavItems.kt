package com.devflow.factum.presentation.component.navbar

import com.devflow.factum.R
import com.devflow.factum.navigation.Destination

enum class NavItems(
    val titleId: Int,
    val activeIconId: Int,
    val passiveIconId: Int,
    val route: Destination
) {
    Home(
        titleId = R.string.home,
        activeIconId = R.drawable.ic_round_home_24,
        passiveIconId = R.drawable.ic_outline_home_24,
        route = Destination.HomeScreen
    ),
    Categories(
        titleId = R.string.categories,
        activeIconId = R.drawable.ic_round_categories_24,
        passiveIconId = R.drawable.ic_outline_categories_24,
        route = Destination.CategoriesScreen
    ),
    Favorite(
        titleId = R.string.favorite,
        activeIconId = R.drawable.ic_round_favorite_24,
        passiveIconId = R.drawable.ic_outline_favorite_24,
        route = Destination.FavoriteScreen
    ),
    Settings(
        titleId = R.string.settings,
        activeIconId = R.drawable.ic_round_settings_24,
        passiveIconId = R.drawable.ic_outline_settings_24,
        route = Destination.SettingsScreen
    )
}