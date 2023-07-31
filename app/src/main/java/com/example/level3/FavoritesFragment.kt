package com.example.level3

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val favoritesList = mutableListOf<FavoriteAnimal>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView = view.findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        favoritesAdapter = FavoritesAdapter(favoritesList, this::deleteFavorite)
        recyclerView.adapter = favoritesAdapter
        loadFavorites()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        favoritesAdapter = FavoritesAdapter(favoritesList, this::deleteFavorite)
        recyclerView.adapter = favoritesAdapter

        loadFavorites()
    }



    override fun onResume() {
        super.onResume()
        loadFavorites() // Refresh the favorites list when the fragment is resumed
    }

     fun isAnimalFavorite(animal: Animal): Boolean {
        val sharedPreferences =
            requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoriteSet: MutableSet<String> =
            sharedPreferences.getStringSet("animal_ids", mutableSetOf()) ?: mutableSetOf()
        return favoriteSet.contains(animal.imageUrl)
    }


    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    internal fun loadFavorites() {
        val favoriteRepository = FavoriteAnimalRepository(
            FavoriteAnimalDatabase.getDatabase(requireContext()).favoriteAnimalDao()
        )

        GlobalScope.launch(Dispatchers.IO) {
            favoritesList.clear()
            favoritesList.addAll(favoriteRepository.getAllFavorites())

            withContext(Dispatchers.Main) {
                favoritesAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    private fun deleteFavorite(favoriteAnimal: FavoriteAnimal) {
        val favoriteRepository = FavoriteAnimalRepository(
            FavoriteAnimalDatabase.getDatabase(requireContext()).favoriteAnimalDao()
        )

        GlobalScope.launch(Dispatchers.IO) {
            favoriteRepository.deleteFavorite(favoriteAnimal)
            withContext(Dispatchers.Main) {
                favoritesList.remove(favoriteAnimal)
                favoritesAdapter.notifyDataSetChanged()
            }
        }
    }
}
