# TMDB-Movie-Tv-Shows-App
This project is intentionally multi-module. The original assessment asks for Clean Architecture and says multi-module is a plus; this implementation makes networking, database, domain, repository and each major feature independent modules.
##Screenshots
<img src="screenshots/Screenshot_1.jpg" width="300" height="200" alt="App Screenshots 1">
<img src="screenshots/Screenshot_2.jpg" width="300" height="200" alt="App Screenshots 2">
<img src="screenshots/Screenshot_3.jpg" width="300" height="200" alt="App Screenshots 3">
<img src="screenshots/Screenshot_4.jpg" width="300" height="200" alt="App Screenshots 4">
## Modules

- `:app` — application shell, Activity, navigation and Compose UI composition.
- `:core:common` — shared constants.
- `:core:network` — TMDB Retrofit API, DTOs, OkHttp and API-key interceptor.
- `:core:database` — Room database/DAO and persistence foundation.
- `:domain` — business models, repository contracts and use cases.
- `:data:repository` — repository implementation and Paging data sources.
- `:features:home` — discovery ViewModel.
- `:features:movies` — movie Paging ViewModel.
- `:features:tv` — TV Paging ViewModel.
- `:features:people` — people Paging ViewModel.
- `:features:search` — global search with debounce.
- `:features:detail` — movie/TV/person detail ViewModels.

## Dependency direction

`app → feature → domain`

`data:repository → domain + core:network + core:database`

Core modules have no dependency on feature modules.

## Setup

Copy `local.properties.example` to `local.properties` and add your TMDB API key:

`TMDB_API_KEY=YOUR_TMDB_API_KEY`

Open the root folder in Android Studio and run the `app` configuration.

## Assessment features

Compose, Kotlin, MVVM, StateFlow/Flow, Retrofit/OkHttp, API-key interceptor, Room foundation, DataStore dependency, Coil, Hilt, Paging 3, debounced search, loading/error states, detail screens, cast, similar content and unit-testable domain boundaries are represented in the module structure.
