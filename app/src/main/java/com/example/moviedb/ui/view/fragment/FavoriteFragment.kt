package com.example.moviedb.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviedb.data.model.RoomSavedItems
import com.example.moviedb.data.source.local.MovieDbDao
import com.example.moviedb.databinding.FragmentFavoriteBinding
import com.example.moviedb.ui.adapter.SaveListAdapter
import com.example.moviedb.ui.view.fragment.detail.DetailsActivity
import com.example.moviedb.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment(), SaveListAdapter.MovieListener {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: SaveListAdapter
    private var saveItemList: MutableList<RoomSavedItems> = mutableListOf()

    @Inject
    lateinit var dao: MovieDbDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        viewInit()
        return binding.root
    }

    private fun viewInit() {

        saveItemList.addAll(dao.getAllSavedItem())

        adapter = SaveListAdapter(this, requireContext(), saveItemList, dao)
        binding.favoriteRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.favoriteRecyclerView.adapter = adapter


    }

    override fun movieItemClick(type: Int, movieId: Int) {
        if (type == 1) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("id", movieId)
            startActivity(intent)
        } else {
            context?.toast("This TvShow Item.")
        }

    }


}