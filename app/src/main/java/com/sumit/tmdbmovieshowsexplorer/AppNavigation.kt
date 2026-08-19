package com.sumit.tmdbmovieshowsexplorer

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val currentDestination = nav.currentBackStackEntryAsState().value?.destination
    val bottomBarRoutes = setOf(
        "home",
        "movies",
        "tv",
        "people",
        "search"
    )
    val showBottomBar = currentDestination?.route in bottomBarRoutes
    Scaffold(
        bottomBar ={
            if(showBottomBar){
                NavigationBar {
                    listOf(
                        "home" to Icons.Default.Home,
                        "moview" to Icons.Default.Movie,
                        "tv" to Icons.Default.Tv,
                        "people" to Icons.Default.Person,
                        "serach" to Icons.Default.Search
                    ).forEach { (r,i)->
                        NavigationBarItem(
                            selected = currentDestination?.route ==r,
                            onClick ={
                                nav.navigate(r){
                                    launchSingleTop =true
                                }
                            },
                            icon ={
                                Icon(i,contentDescription = null)
                            },
                            label ={
                                Text(r.replaceFirstChar { it.uppercase() })
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) {
        padding ->
        NavHost(
            navController =nav,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) { }
    }
}