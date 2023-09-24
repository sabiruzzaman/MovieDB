package com.example.moviedb.paging

import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.example.moviedb.databinding.ItemProgressBinding


class LoaderAdapter : LoadStateAdapter<LoaderAdapter.LoadViewHolder>() {

    class LoadViewHolder(private val binding: ItemProgressBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindState(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
        }

    }

    override fun onBindViewHolder(holder: LoadViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadViewHolder {
        return LoadViewHolder(
            ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

}