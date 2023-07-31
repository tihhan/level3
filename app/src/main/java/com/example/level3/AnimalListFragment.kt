package com.example.level3

import android.annotation.SuppressLint
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
import org.json.JSONArray
import java.net.URL

class AnimalListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val animalList = mutableListOf<Animal>()
    private lateinit var animalAdapter: AnimalAdapter


    private var isLoading = false
    private var currentPage = 1
    private val itemsPerPage = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animal_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        animalAdapter = AnimalAdapter(requireContext(), animalList, this::toggleFavorite)
        recyclerView.adapter = animalAdapter
        setupPagination()
        fetchAnimals()
        return view
    }

    private fun setupPagination() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (!isLoading && lastVisibleItemPosition == totalItemCount - 1) {
                    // Load more items when reaching the end of the list
                    currentPage++
                    fetchAnimals()
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchAnimals() {
        isLoading = true
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val json =
                    URL("https://shibe.online/api/shibes?count=$itemsPerPage&page=$currentPage&urls=true&httpsUrls=true")
                        .readText()
                val jsonArray = JSONArray(json)
                for (i in 0 until jsonArray.length()) {
                    val imageUrl = jsonArray.getString(i)
                    animalList.add(Animal(imageUrl))
                }
                withContext(Dispatchers.Main) {
                    animalAdapter.notifyDataSetChanged()
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
            }
        }
    }

    private var favoritesFragment: FavoritesFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the favoritesFragment variable
        favoritesFragment = parentFragmentManager.findFragmentByTag("FavoritesFragment") as? FavoritesFragment
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun toggleFavorite(animal: Animal) {
        val favoriteRepository = FavoriteAnimalRepository(
            FavoriteAnimalDatabase.getDatabase(requireContext()).favoriteAnimalDao()
        )

        GlobalScope.launch(Dispatchers.IO) {
            val favoriteAnimal = FavoriteAnimal(animal.imageUrl)

            if (favoritesFragment?.isAnimalFavorite(animal) == true) {
                favoriteRepository.deleteFavorite(favoriteAnimal)
            } else {
                favoriteRepository.insertFavorite(favoriteAnimal)
            }
        }
    }







}
