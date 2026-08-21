package com.sumit.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert

@Entity(tableName = "cached_media")
data class CachedMedia(
    @PrimaryKey val id: Int,
    val title: String,
    val poster: String?,
    val date: String?,
    val rating: Double,
    val type: String,
    val category: String,
    val page: Int = 0,
    val cachedAt: Long= System.currentTimeMillis()
)

@Entity(tableName = "cached_people")
data class CachedPerson(
    @PrimaryKey val id: Int,
    val name: String,
    val profilePath: String?,
    val knownFor: String,
    val category: String="POPULAR",
    val page: Int=0,
    val cachedAt: Long= System.currentTimeMillis()
)

@Entity(tableName = "cached_details")
data class CachedDetails(
    @PrimaryKey val id: Int,
    val title: String,
    val tagline: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val date: String?,
    val runtime: String?,
    val rating: Double,
    val genres: String,
    val type: String,
    val cachedAt: Long= System.currentTimeMillis()
)
@Entity(tableName = "cached_person_detail")
data class CachedPersonDetail(
    @PrimaryKey val id: Int,
    val name: String,
    val biography: String?,
    val birthday: String?,
    val birthplace: String?,
    val profilePath: String?,
    val cachedAt: Long= System.currentTimeMillis()
)



@Dao
interface MovieDao{
    @Query("SELECT * FROM cached_media where type = :type AND category = :category ORDER BY page ASC, id ASC")
    suspend fun getMedia(type: String, category: String): List<CachedMedia>

    @Upsert
    suspend fun upsertMedia(items: List<CachedMedia>)

    @Query("DELETE FROM cached_media WHERE type = :type AND category = :category")
    suspend fun deleteMedia(type: String,category: String)

    @Query("SELECT * FROM  cached_people WHERE category = :category ORDER BY page ASC, id ASC")
    suspend fun getPeople(category: String): List<CachedPerson>

    @Upsert
    suspend fun upsertPeople(items: List<CachedPerson>)

    @Query("SELECT * FROM cached_person_detail WHERE id = :id")
    suspend fun getPersonDetail(id: Int): CachedPersonDetail?

    @Upsert
    suspend fun upsertPersonDetail(detail: CachedPersonDetail)

    @Query("SELECT * FROM cached_details WHERE id = :id AND type = :type")
    suspend fun getDetail(id: Int,type: String): CachedDetails

    @Upsert
    suspend fun upsertDetail(details: CachedDetails)
}

@Database(entities = [CachedMedia::class,CachedPerson::class,CachedDetails::class,CachedPersonDetail::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
    abstract fun movieDao(): MovieDao
}