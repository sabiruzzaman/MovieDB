package com.example.moviedb.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.moviedb.R
import com.example.moviedb.data.model.RoomSavedItems
import com.example.moviedb.data.model.TvShowListResponse
import com.example.moviedb.data.source.local.MovieDbDao
import com.example.moviedb.databinding.ItemMoviesBinding
import com.example.moviedb.utils.Constants
import com.example.moviedb.utils.datePrettier

class TvShowAdapter(private var dao: MovieDbDao) :
    PagingDataAdapter<TvShowListResponse.Results, TvShowAdapter.RelatedMovieViewHolder>(Comparator) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedMovieViewHolder {

        val binding = ItemMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RelatedMovieViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RelatedMovieViewHolder, position: Int) {
        getItem(position)?.let {

            val poster = Constants.IMG_PREFIX + it.poster_path
            holder.binding.movieImg.load(poster) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                transformations(RoundedCornersTransformation())
            }
            holder.binding.movieName.text = it.name
            if (it.first_air_date != "") {
                holder.binding.releaseDate.text = it.first_air_date.datePrettier()
            }

            holder.binding.checkBox.isChecked = dao.isItemInSaved(it.id)
            holder.binding.checkBox.setOnClickListener { _ ->
                if (holder.binding.checkBox.isChecked) dao.addItemToSaved(
                    RoomSavedItems(
                        itemId = it.id,
                        title = it.name,
                        release_date = it.first_air_date,
                        poster_path = it.poster_path,
                        type = 1
                    )
                )
                else dao.deleteFromSaved(itemId = it.id)
            }


        }

    }


    inner class RelatedMovieViewHolder(val binding: ItemMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)


    companion object {
        private val Comparator =
            object : DiffUtil.ItemCallback<TvShowListResponse.Results>() {
                override fun areItemsTheSame(
                    oldItem: TvShowListResponse.Results,
                    newItem: TvShowListResponse.Results
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: TvShowListResponse.Results,
                    newItem: TvShowListResponse.Results
                ): Boolean {
                    return oldItem == newItem
                }
            }

    }


}