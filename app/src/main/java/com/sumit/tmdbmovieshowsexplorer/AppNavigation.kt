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
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sumit.details.DetailsViewModel
import com.sumit.details.PersonViewModel
import com.sumit.domain.model.MediaType
import com.sumit.home.HomeViewModel
import com.sumit.movies.MoviesViewModel
import com.sumit.people.PeopleViewModel
import com.sumit.search.SearchViewModel
import com.sumit.tmdbmovieshowsexplorer.ui.DetailsScreen
import com.sumit.tmdbmovieshowsexplorer.ui.HomeSceen
import com.sumit.tmdbmovieshowsexplorer.ui.MoviesScreen
import com.sumit.tmdbmovieshowsexplorer.ui.PeopleScreen
import com.sumit.tmdbmovieshowsexplorer.ui.PersonScreen
import com.sumit.tmdbmovieshowsexplorer.ui.SearchScreen
import com.sumit.tmdbmovieshowsexplorer.ui.TvScreen
import com.sumit.tv.TvViewModel

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
                        "movies" to Icons.Default.Movie,
                        "tv" to Icons.Default.Tv,
                        "people" to Icons.Default.Person,
                        "search" to Icons.Default.Search
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
        ) {
            composable("home") {
                HomeSceen(nav, hiltViewModel<HomeViewModel>())
            }
            composable("movies") {
                MoviesScreen(nav,hiltViewModel<MoviesViewModel>())
            }
            composable("tv") {
                TvScreen(nav,hiltViewModel<TvViewModel>())
            }
            composable("people") {
                PeopleScreen(nav,hiltViewModel<PeopleViewModel>())
            }
            composable("search") {
                SearchScreen(nav,hiltViewModel<SearchViewModel>())
            }
            composable("movie/{id}",
                listOf(navArgument("id"){
                    type= NavType.IntType
                })) {
                DetailsScreen(nav,hiltViewModel<DetailsViewModel>(),
                    it.arguments!!.getInt("id"),
                    MediaType.MOVIE)
            }
            composable ("tv/{id}",
                listOf(navArgument("id"){
                    type = NavType.IntType
                })){
                DetailsScreen(nav,hiltViewModel<DetailsViewModel>(),
                    it.arguments!!.getInt("id"),
                    MediaType.TV)
            }
            composable("person/{id}",listOf(navArgument("id"){
                type= NavType.IntType
            })) {
                PersonScreen(nav,hiltViewModel<PersonViewModel>(),
                    it.arguments!!.getInt("id"))
            }
        }
    }
}