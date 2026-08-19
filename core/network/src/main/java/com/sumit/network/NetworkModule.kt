package com.sumit.network

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{
    @Provides
    @Singleton
    fun client(): OkHttpClient = OkHttpClient.Builder().addInterceptor { c ->
        val u = c.request().url.newBuilder().addQueryParameter("api_key",BuildConfig.TMDB_API_KEY)
            .addQueryParameter("language","en-US").build()
        c.proceed(c.request().newBuilder().url(u).build())
    }.retryOnConnectionFailure(true).build()

    @Provides
    @Singleton
    fun api(client: OkHttpClient): TmdbApi{
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TmdbApi::class.java)
    }
}