package com.example.movierama.ui.features.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.ItemSearchMovieSuggestionBinding
import com.example.movierama.model.search_movie.SearchSuggestion
import timber.log.Timber

class SearchMovieSuggestionAdapter(
    private val onClick: (movie: SearchSuggestion) -> Unit,
) : ListAdapter<SearchSuggestion, SearchMovieSuggestionAdapter.SearchSuggestionViewHolder>(
    DiffCallback()
) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchMovieSuggestionBinding.inflate(inflater, parent, false)
        context = parent.context
        return SearchSuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchSuggestionViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class SearchSuggestionViewHolder(private val binding: ItemSearchMovieSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: SearchSuggestion) {
            binding.root.text = suggestion.query
            Timber.d("now text has ${binding.root.text}")

            binding.root.setOnClickListener {
                onClick(suggestion)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<SearchSuggestion>() {
        override fun areItemsTheSame(
            oldItem: SearchSuggestion,
            newItem: SearchSuggestion,
        ): Boolean {
            return oldItem.query == newItem.query
        }

        override fun areContentsTheSame(
            oldItem: SearchSuggestion,
            newItem: SearchSuggestion,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
