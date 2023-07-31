package com.example.level3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AnimalAdapter(
    private val context: Context,
    private val animals: List<Animal>,
    private val toggleFavorite: (Animal) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val favoriteImageView: ImageView = itemView.findViewById(R.id.favoriteImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.animal_item, parent, false)
        return AnimalViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animals[position]
        Glide.with(holder.itemView.context)
            .load(animal.imageUrl)
            .into(holder.imageView)


        val favoriteIconResId =
            if (animal.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_empty
        holder.favoriteImageView.setImageResource(favoriteIconResId)

        // Set click listener for the favorite icon
        holder.favoriteImageView.setOnClickListener {
            animal.isFavorite = !animal.isFavorite // Toggle the favorite state
            toggleFavorite(animal)
            // Update the favorite icon after toggling the favorite status
            val updatedFavoriteIconResId =
                if (animal.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_empty
            holder.favoriteImageView.setImageResource(updatedFavoriteIconResId)
        }
    }



    override fun getItemCount(): Int {
        return animals.size
    }

}
