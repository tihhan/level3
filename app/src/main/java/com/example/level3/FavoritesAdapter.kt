package com.example.level3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavoritesAdapter(
    private val favoritesList: List<FavoriteAnimal>,
    private val deleteFavorite: (FavoriteAnimal) -> Unit
    ) :
    RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.animal_item, parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteAnimal = favoritesList[position]
        Glide.with(holder.itemView.context)
            .load(favoriteAnimal.imageUrl)
            .into(holder.imageView)

        // Set click listener for the favorite image to delete it
        holder.imageView.setOnClickListener {
            deleteFavorite(favoriteAnimal)
        }
    }


    override fun getItemCount(): Int {
        return favoritesList.size
    }
}
