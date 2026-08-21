# TMDB-Movie-Tv-Shows-App
This project is intentionally multi-module. The original assessment asks for Clean Architecture and says multi-module is a plus; this implementation makes networking, database, domain, repository and each major feature independent modules.
##Screenshots
![Screenshot_1.jpg](screenshots/Screenshot_1.jpg)
![Screenshot_2.jpg](screenshots/Screenshot_2.jpg)![Screenshot_3.jpg](screenshots/Screenshot_3.jpg)
![Screenshot_4.jpg](screenshots/Screenshot_4.jpg)
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
