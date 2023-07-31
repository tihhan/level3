package com.example.level3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteAnimal::class], version = 1)
abstract class FavoriteAnimalDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteAnimalDatabase? = null

        fun getDatabase(context: Context): FavoriteAnimalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteAnimalDatabase::class.java,
                    "favorite_animal_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
