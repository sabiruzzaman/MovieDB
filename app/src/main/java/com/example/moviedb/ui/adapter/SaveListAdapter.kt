package com.example.moviedb.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.moviedb.R
import com.example.moviedb.data.model.RoomSavedItems
import com.example.moviedb.data.source.local.MovieDbDao
import com.example.moviedb.databinding.ItemMoviesBinding
import com.example.moviedb.utils.Constants
import com.example.moviedb.utils.datePrettier


class SaveListAdapter(
    private var movieListener: MovieListener,
    val context: Context,
    private val savedItems: MutableList<RoomSavedItems>,
    private var dao: MovieDbDao
) :
    RecyclerView.Adapter<SaveListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemMoviesBinding.bind(itemView)
    }

    interface MovieListener {
        fun movieItemClick(type: Int, movieId: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val saveItems = savedItems[position]

        val poster = Constants.IMG_PREFIX + saveItems.poster_path
        holder.binding.movieImg.load(poster) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            transformations(RoundedCornersTransformation())
        }
        holder.binding.movieName.text = saveItems.title
        if (saveItems.release_date != "") {
            holder.binding.releaseDate.text = saveItems.release_date.datePrettier()
        }

        holder.binding.checkBox.isChecked = dao.isItemInSaved(saveItems.itemId)

        holder.binding.checkBox.setOnClickListener() {
            if (holder.binding.checkBox.isChecked) dao.addItemToSaved(
                RoomSavedItems(
                    itemId = saveItems.itemId,
                    title = saveItems.title,
                    release_date = saveItems.release_date,
                    poster_path = saveItems.poster_path,
                    type = saveItems.type
                )
            )
            else {
                dao.deleteFromSaved(itemId = saveItems.itemId)
                savedItems.removeAt(position)
                notifyItemRemoved(position)
            }
        }

        /* holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
             if (isChecked) dao.addItemToSaved(
                 RoomSavedItems(
                     itemId = saveItems.itemId,
                     title = saveItems.title,
                     release_date = saveItems.release_date,
                     poster_path = saveItems.poster_path,
                     type = saveItems.type
                 )
             )
             else {
                 dao.deleteFromSaved(itemId = saveItems.itemId)
                 savedItems.removeAt(position)
                 notifyItemRemoved(position)
             }
         }*/


        holder.itemView.setOnClickListener { _ ->
            movieListener.movieItemClick(saveItems.type, saveItems.itemId)
        }


    }

    override fun getItemCount(): Int {
        return savedItems.size
    }

}