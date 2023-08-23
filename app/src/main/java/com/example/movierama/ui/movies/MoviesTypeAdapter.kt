package com.example.movierama.ui.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.ItemMoviesTypeBinding

/**
 * The MoviesTypeAdapter class is a RecyclerView adapter for displaying movie types.
 * It enables single selection behavior, allowing users to pick one item at a time.
 * The isActivated state is utilized to indicate the selected item, bypassing the elevation
 * animation conflict associated with isSelected.
 * */
class MoviesTypeAdapter(
    private val items: List<MoviesType> = MoviesType.values().toList(),
    private val onClick: (movie: MoviesType) -> Unit // Callback to handle item click
) :
    RecyclerView.Adapter<MoviesTypeAdapter.MovieViewHolder>() {

    private lateinit var context: Context
    private var selectedPosition = 0 // By default first type is selected

    // Inflate the item view layout and initialize the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMoviesTypeBinding.inflate(
            inflater,
            parent,
            false
        ) // Replace with actual binding initialization
        context = parent.context
        return MovieViewHolder(binding)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val moviesType = items[position]
        holder.bind(moviesType, position)
    }

    override fun getItemCount(): Int = items.size

    inner class MovieViewHolder(private val binding: ItemMoviesTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind data to the view holder's views
        fun bind(type: MoviesType, position: Int) {
            binding.typeDescription.text = type.description

            // Handle item click
            binding.root.setOnClickListener {
                onClick(type) // Invoke the provided callback with the clicked item

                // Update selected item and notify data changes
                val previousSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedPosition)
            }

            // Highlight the selected item by setting its activated state
            binding.root.isActivated = position == selectedPosition
        }
    }
}
