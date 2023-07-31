package com.example.level3

class FavoriteAnimalRepository(private val favoriteAnimalDao: FavoriteAnimalDao) {

    suspend fun insertFavorite(favorite: FavoriteAnimal) {
        favoriteAnimalDao.insertFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: FavoriteAnimal) {
        favoriteAnimalDao.deleteFavorite(favorite)
    }

    suspend fun getAllFavorites(): List<FavoriteAnimal> {
        return favoriteAnimalDao.getAllFavorites()
    }
}
