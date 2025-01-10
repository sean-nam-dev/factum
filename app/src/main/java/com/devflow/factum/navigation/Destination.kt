package com.devflow.factum.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object StartGraph: Destination

    @Serializable
    data object HomeGraph: Destination

    @Serializable
    data object StartScreen: Destination

    @Serializable
    data object HomeScreen: Destination {
        override fun toString(): String {
            return "${this::class.qualifiedName}"
        }
    }

    @Serializable
    data object CategoriesScreen: Destination {
        override fun toString(): String {
            return "${this::class.qualifiedName}"
        }
    }

    @Serializable
    data object FavoriteScreen: Destination {
        override fun toString(): String {
            return "${this::class.qualifiedName}"
        }
    }

    @Serializable
    data object SettingsScreen: Destination {
        override fun toString(): String {
            return "${this::class.qualifiedName}"
        }
    }

    @Serializable
    data class DetailScreen(
        val index: Int,
        val localIndex: Int
    ): Destination

    @Serializable
    data class CategoryScreen(
        val documentName: String
    ): Destination

    @Serializable
    data class CategoryDetailScreen(
        val index: Int,
        val localIndex: Int,
        val category: String
    ): Destination

    @Serializable
    data class FavoriteDetailScreen(
        val index: Int,
        val localIndex: Int
    ): Destination

    @Serializable
    data object NotificationScreen: Destination {
        override fun toString(): String {
            return "${this::class.qualifiedName}"
        }
    }

    @Serializable
    data object FavoriteCategoriesScreen: Destination {
        override fun toString(): String {
            return "${this::class.qualifiedName}"
        }
    }

    @Serializable
    data class DeepLinkScreen(
        val id: String,
        val category: String
    ): Destination
}