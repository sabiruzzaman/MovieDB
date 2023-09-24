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
import com.example.moviedb.data.model.RelatedMovieListResponse
import com.example.moviedb.databinding.ItemRelatedMovieBinding
import com.example.moviedb.utils.Constants
import com.example.moviedb.utils.datePrettier


class RelatedMoviesAdapter() :
    PagingDataAdapter<RelatedMovieListResponse.Results, RelatedMoviesAdapter.RelatedMovieViewHolder>(
        Comparator
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedMovieViewHolder {

        val binding =
            ItemRelatedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            holder.binding.movieName.text = it.title
            if (it.release_date != "") {
                holder.binding.releaseDate.text = it.release_date.datePrettier()
            }


        }

    }


    inner class RelatedMovieViewHolder(val binding: ItemRelatedMovieBinding) :
        RecyclerView.ViewHolder(binding.root)


    companion object {
        private val Comparator =
            object : DiffUtil.ItemCallback<RelatedMovieListResponse.Results>() {
                override fun areItemsTheSame(
                    oldItem: RelatedMovieListResponse.Results,
                    newItem: RelatedMovieListResponse.Results
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: RelatedMovieListResponse.Results,
                    newItem: RelatedMovieListResponse.Results
                ): Boolean {
                    return oldItem == newItem
                }
            }

    }


}