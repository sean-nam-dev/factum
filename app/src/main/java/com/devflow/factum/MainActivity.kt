package com.devflow.factum

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.NavigationAction
import com.devflow.factum.navigation.Navigator
import com.devflow.factum.presentation.component.navbar.BottomNavigationBar
import com.devflow.factum.presentation.component.topbar.ContentAwareTopAppBar
import com.devflow.factum.util.ObserveAsEvents
import com.devflow.factum.presentation.component.snackbar.SnackbarController
import com.devflow.factum.presentation.component.snackbar.SnackbarEvent
import com.devflow.factum.presentation.screen.categories.CategoriesUIScreen
import com.devflow.factum.presentation.screen.category.CategoryUIScreen
import com.devflow.factum.presentation.screen.category.CategoryViewModel
import com.devflow.factum.presentation.screen.category_detail.CategoryDetailUIScreen
import com.devflow.factum.presentation.screen.detail.DetailUIScreen
import com.devflow.factum.presentation.screen.favorite.FavoriteUIScreen
import com.devflow.factum.presentation.screen.favorite.FavoriteViewModel
import com.devflow.factum.presentation.screen.favorite_categories.FavoriteCategoriesUIScreen
import com.devflow.factum.presentation.screen.favorite_detail.FavoriteDetailUIScreen
import com.devflow.factum.presentation.screen.home.HomeUIScreen
import com.devflow.factum.presentation.screen.home.HomeViewModel
import com.devflow.factum.presentation.screen.settings.SettingsUIScreen
import com.devflow.factum.presentation.screen.settings.SettingsViewModel
import com.devflow.factum.presentation.screen.start.StartUIScreen
import com.devflow.factum.ui.theme.FactumTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FactumTheme { // JunkHomeTest()
                val scope = rememberCoroutineScope()

                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                val snackBarHostState = remember { SnackbarHostState() }

                ObserveAsEvents(
                    flow = navigator.navigationActions
                ) { action ->
                    when(action) {
                        is NavigationAction.Navigate -> {
                            navController.navigate(action.destination) {
                                action.navOptions(this)
                            }
                        }
                        NavigationAction.NavigateUp -> {
                            navController.navigateUp()
                        }
                    }
                }

                ObserveAsEvents(
                    flow = SnackbarController.events,
                    snackBarHostState
                ) { event ->
                    scope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()

                        val result = snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.name,
                            duration = event.duration
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }
                Scaffold(
                    topBar = {
                        ContentAwareTopAppBar(
                            navigator = navigator,
                            backStackEntry = currentBackStackEntry
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(
                            currentRoute = currentRoute,
                            navigator = navigator
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackBarHostState
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = navigator.startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        navigation<Destination.StartGraph>(
                            startDestination = Destination.StartScreen
                        ) {
                            composable<Destination.StartScreen> {
                                StartUIScreen()
                            }
                        }
                        navigation<Destination.HomeGraph>(
                            startDestination = Destination.HomeScreen
                        ) {
                            composable<Destination.HomeScreen> { backStackEntry ->
                                val viewModel: HomeViewModel = hiltViewModel(backStackEntry)

                                HomeUIScreen(viewModel)
                            }
                            composable<Destination.DetailScreen> {
                                val args = it.toRoute<Destination.DetailScreen>()
                                val viewModel: HomeViewModel =
                                    if (navController.previousBackStackEntry != null) {
                                        hiltViewModel(navController.previousBackStackEntry!!)
                                    } else {
                                        hiltViewModel()
                                    }

                                DetailUIScreen(
                                    viewModel = viewModel,
                                    index = args.index,
                                    localIndex = args.localIndex
                                )
                            }
                            composable<Destination.CategoriesScreen> {
                                CategoriesUIScreen()
                            }
                            composable<Destination.CategoryScreen> {
                                val args = it.toRoute<Destination.CategoryScreen>()
                                val viewModel: CategoryViewModel = hiltViewModel(
                                    creationCallback = { factory: CategoryViewModel.CategoryViewModelFactory ->
                                        factory.create(args.documentName)
                                    }
                                )

                                CategoryUIScreen(
                                    viewModel = viewModel
                                )
                            }
                            composable<Destination.CategoryDetailScreen> {
                                val args = it.toRoute<Destination.CategoryDetailScreen>()
                                val viewModel: CategoryViewModel =
                                    if (navController.previousBackStackEntry != null) {
                                        hiltViewModel(
                                            navController.previousBackStackEntry!!,
                                            creationCallback = { factory: CategoryViewModel.CategoryViewModelFactory ->
                                                factory.create(args.category)
                                            }
                                        )
                                    } else {
                                        hiltViewModel(
                                            creationCallback = { factory: CategoryViewModel.CategoryViewModelFactory ->
                                                factory.create(args.category)
                                            }
                                        )
                                    }

                                CategoryDetailUIScreen(
                                    viewModel = viewModel,
                                    index = args.index,
                                    localIndex = args.localIndex
                                )
                            }
                            composable<Destination.FavoriteScreen> {
                                FavoriteUIScreen()
                            }
                            composable<Destination.FavoriteDetailScreen> {
                                val args = it.toRoute<Destination.FavoriteDetailScreen>()
                                val viewModel: FavoriteViewModel =
                                    if (navController.previousBackStackEntry != null) {
                                        hiltViewModel(navController.previousBackStackEntry!!)
                                    } else {
                                        hiltViewModel()
                                    }

                                FavoriteDetailUIScreen(
                                    viewModel = viewModel,
                                    index = args.index,
                                    localIndex = args.localIndex
                                )
                            }
                            composable<Destination.SettingsScreen> {
                                SettingsUIScreen()
                            }
                            composable<Destination.NotificationScreen> {
                                val viewModel: SettingsViewModel =
                                    if (navController.previousBackStackEntry != null) {
                                        hiltViewModel(navController.previousBackStackEntry!!)
                                    } else {
                                        hiltViewModel()
                                    }

                                val context = this@MainActivity
                                val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                                val notificationHandler = NotificationHandler(context)

                                LaunchedEffect(key1 = true) {
                                    if (!postNotificationPermission.status.isGranted) postNotificationPermission.launchPermissionRequest()
                                }

                                Column {
                                    Button(onClick = { notificationHandler.showSimpleNotification() }) {
                                        Text(text = "Simple notification trigger")
                                    }
                                }
                            }
                            composable<Destination.FavoriteCategoriesScreen> {
                                val viewModel: SettingsViewModel =
                                    if (navController.previousBackStackEntry != null) {
                                        hiltViewModel(navController.previousBackStackEntry!!)
                                    } else {
                                        hiltViewModel()
                                    }

                                FavoriteCategoriesUIScreen(
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}