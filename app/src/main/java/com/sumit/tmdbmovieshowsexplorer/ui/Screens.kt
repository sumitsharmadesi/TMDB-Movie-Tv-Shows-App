package com.sumit.tmdbmovieshowsexplorer.ui

import android.util.Log
import android.widget.Button
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.sumit.common.Constants
import com.sumit.details.DetailsViewModel
import com.sumit.details.PersonViewModel
import com.sumit.domain.model.MediaItem
import com.sumit.domain.model.MediaType
import com.sumit.home.HomeViewModel
import com.sumit.movies.MoviesViewModel
import com.sumit.people.PeopleViewModel
import com.sumit.search.SearchViewModel
import com.sumit.tv.TvViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeSceen(nav: NavController,viewModel: HomeViewModel){
    val state by viewModel.state.collectAsState()
    LazyColumn(Modifier.padding(16.dp)) {
        item {
            Text(
                "Discover",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        if(state.loading) item {
            CircularProgressIndicator(Modifier.padding(20.dp))
        }
        state.error.let {
            item {
                if (it != null) {
                    Text(it)
                    Button(viewModel::refresh){ Text("Retry")}
                }
            }
        }
        item {
            Section("Trending Movies",state.movies,nav)
            Section("Trending Tv",state.tv,nav)
        }
    }
}

@Composable
fun Section(title: String,items: List<MediaItem>,nav: NavController){
    Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 10.dp)
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)){
        items(items){
            CardItem(it) {
                open(nav,it)
            }
        }
    }
}

@Composable
fun CardItem(media: MediaItem, on:()-> Unit){
    Card(
        onClick =on,
        Modifier.width(145.dp)
    ) {
        Column {
            AsyncImage(
                "${Constants.IMAGE_URL}${media.posterPath}",
                media.title,
                Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop,
                onError ={
                    Log.e("Image Error","Image Error",it.result.throwable)
                }
            )
            Text(
                media.title,
                Modifier.padding(8.dp).height(48.dp),
                maxLines = 2,
                fontWeight = FontWeight.SemiBold
            )
            Text("⭐ ${"%.1f".format(media.rating)}")
            Modifier.padding(8.dp)
        }
    }
}
@Composable
fun MoviesScreen(nav: NavController,viewModel: MoviesViewModel)=PagingScreen(
    "Movies",
    listOf("Popular" to viewModel.popular,"Top Rated" to viewModel.topRated,"Upcoming" to viewModel.upcoming),
    nav
)
@Composable
fun TvScreen(nav: NavController,viewModel: TvViewModel)=PagingScreen(
    "Tv Shows",
    listOf("Popular" to viewModel.popular,"Top Rated" to viewModel.topRated,"Airing Today" to viewModel.upcoming),
    nav
)

@Composable
fun PeopleScreen(nav: NavController,viewModel: PeopleViewModel){
    val peoples =viewModel.people.collectAsLazyPagingItems()
    LazyColumn(Modifier.padding(12.dp)) {
        item {
            Text(
                "Popular People",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        items(peoples.itemCount){ i ->
            peoples[i]?.let { p ->
                ListItem(
                    headlineContent = { Text(p.name)},
                    supportingContent = { Text(p.knownFor)},
                    leadingContent ={
                        AsyncImage(
                            "${Constants.IMAGE_URL}${p.profilePath}",
                            p.name,
                            Modifier.size(55.dp).clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )
                    },
                    modifier = Modifier.clickable{ nav.navigate("person/${p.id}")}
                )
            }
        }
    }
}

@Composable
fun SearchScreen(nav: NavController, viewModel: SearchViewModel){
    var query by remember { mutableStateOf("") }
    val result  =  viewModel.results.collectAsState()
    Column(Modifier.padding(16.dp)) {
        Text(
            "Search",
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            query,
            {
                query = it
                viewModel.setQuery(it)
            },
            Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Movies, Tv Shows & People")}
        )
        LazyColumn {
            items(result.value){
                ListItem(
                    headlineContent = { Text(it.title)},
                    supportingContent = { Text(it.type.name)},
                    modifier = Modifier.clickable{
                        open(nav,it)
                    }
                )
            }
        }
    }
}

@Composable
fun DetailsScreen(nav: NavController,viewModel: DetailsViewModel,id: Int,type: MediaType){
    LaunchedEffect(id) { viewModel.load(id,type) }
    val state by viewModel.state.collectAsState()
    val detail = state.detail
    if(state.loading){
        CircularProgressIndicator(Modifier.padding(20.dp))
    }else if(detail==null){
        Text(state.error.orEmpty(), Modifier.padding(20.dp))
    }else{
        LazyColumn {
            item {
                AsyncImage(
                    "${Constants.IMAGE_URL}${detail.backDropPath}",
                    null,
                    Modifier.fillMaxWidth().height(230.dp),
                    contentScale = ContentScale.Crop
                )
            }
            item {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        detail.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "⭐ ${"%.1f".format(detail.rating)} • ${detail.date.orEmpty()} • ${detail.runtime.orEmpty()}"
                    )
                    Text(
                        detail.tagline.orEmpty()
                    )
                    Text(
                        detail.overview.orEmpty(),
                        Modifier.padding(vertical = 12.dp)
                    )
                    Text(
                        "Genres: ${detail.genres.joinToString()}"
                    )
                    Text(
                        "Cast",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    LazyRow {
                        items(detail.cast) {
                            Text(
                                it.name,
                                Modifier
                                    .padding(8.dp)
                                    .clickable { nav.navigate("person/${it.id}") })
                        }
                    }
                    Text(
                        "Similar",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    LazyRow {
                        items(detail.similar){
                            CardItem(it) {
                                open(nav,it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonScreen(nav: NavController,viewModel: PersonViewModel,id: Int){
    LaunchedEffect(id) {viewModel.load(id) }
    val state by viewModel.state.collectAsState()
    val person = state.detail
    if(state.loading){
        CircularProgressIndicator(Modifier.padding(20.dp))
    }else if(person==null){
        Text(
            state.error.orEmpty(),
            Modifier.padding(20.dp)
        )
    }else{
        LazyColumn(Modifier.padding(16.dp)) {
            item {
                AsyncImage(
                    "${Constants.IMAGE_URL}${person.profilePath}",
                    person.name,
                    Modifier.size(150.dp).clip(MaterialTheme.shapes.large),
                    contentScale = ContentScale.Crop
                )
                Text(
                    person.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Born: ${person.birthday?:"Unknown"}"
                )
                Text(
                    "Birthplace: ${person.birthPlace?:"Unknown"}"
                )
                Text(
                    "Biography",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    person.biography?:"No biography available"
                )
            }
            item {
                Text(
                    "Known For",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                LazyRow {
                    items(person.knownFor){
                        CardItem(it) {
                            open(nav,it)
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun PagingScreen(
    title: String,
    sections: List<Pair<String, Flow<PagingData<MediaItem>>>>,
    n: NavController
) {
    LazyColumn(Modifier.padding(12.dp)) {
        item {
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium
            )
        }; sections.forEach { (t, f) ->
        item {
            Text(
                t,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            );
            val x = f.collectAsLazyPagingItems(); LazyRow(
            horizontalArrangement = Arrangement.spacedBy(
                10.dp
            )
        ) { items(x.itemCount) { i -> x[i]?.let {
            CardItem(it) {
                open(
                    n,
                    it
                )
            }
        } } }
        }
    }
    }
}

private fun open(n: NavController, m: MediaItem) {
    when (m.type) {
        MediaType.MOVIE -> n.navigate("movie/${m.id}"); MediaType.TV -> n.navigate("tv/${m.id}"); MediaType.PERSON -> n.navigate(
        "person/${m.id}"
    )
    }
}