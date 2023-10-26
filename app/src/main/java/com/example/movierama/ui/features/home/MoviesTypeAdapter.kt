package com.example.movierama.ui.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.ItemMoviesTypeBinding
import com.example.movierama.model.MoviesType
import com.example.movierama.model.SelectedType
import com.example.movierama.model.mapMoviesTypeToSelectedTypeList

class MoviesTypeAdapter(
    private val items: List<SelectedType> = mapMoviesTypeToSelectedTypeList(),
    private val onClick: (movie: MoviesType) -> Unit, // Callback to handle item click
) : RecyclerView.Adapter<MoviesTypeAdapter.MovieViewHolder>() {

    private lateinit var context: Context

    // Inflate the item view layout and initialize the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMoviesTypeBinding.inflate(
            inflater,
            parent,
            false
        )
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val moviesType = items[position]
        holder.bind(moviesType)
    }

    override fun getItemCount(): Int = items.size

    inner class MovieViewHolder(private val binding: ItemMoviesTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind data to the view holder's views
        fun bind(type: SelectedType) {
            binding.typeDescription.text = type.type.description

            // Highlight the selected item by setting its activated state
            binding.root.isActivated = type.isSelected


            binding.root.setOnClickListener {
                onClick(type.type) // Invoke the provided callback with the clicked item, this will eventually trigger setSelectedType
            }
        }
    }

    // Use this method to set the selected type explicitly
    // Iterates items and selects the new selectedType and unselects the previous one
    fun setSelectedType(newSelectedType: MoviesType) {
        items.forEachIndexed { index, selectedType ->
            when {
                selectedType.type == newSelectedType -> {
                    selectedType.isSelected = true
                    notifyItemChanged(index)
                }

                selectedType.isSelected -> {
                    selectedType.isSelected = false
                    notifyItemChanged(index)
                }
            }
        }
    }
}
