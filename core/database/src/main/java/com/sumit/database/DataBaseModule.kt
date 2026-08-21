package com.sumit.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun db(@ApplicationContext c: Context) = Room.databaseBuilder(c, AppDatabase::class.java,"tmdb.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun dao(database: AppDatabase) = database.movieDao()
}