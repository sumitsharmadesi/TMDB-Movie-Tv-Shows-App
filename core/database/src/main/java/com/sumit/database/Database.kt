package com.sumit.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert

@Entity
data class CachedMovie(
    @PrimaryKey val id:Int,
    val title: String,
    val poster: String?,
    val category: String,
    val page: Int,
    val cachedAt: Long= System.currentTimeMillis()
)

@Dao
interface MovieDao{
    @Query("Select * FROM CachedMovie where category=:c ORDER BY page,id")
    suspend fun get(c: String): List<CachedMovie>

    @Upsert
    suspend fun put(items: List<CachedMovie>)
}

@Database(entities = [CachedMovie::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
    abstract fun movieDao(): MovieDao
}