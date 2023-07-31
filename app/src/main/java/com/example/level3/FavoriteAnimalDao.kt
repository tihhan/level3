package com.example.level3

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteAnimalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteAnimal)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteAnimal)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavoriteAnimal>
}
