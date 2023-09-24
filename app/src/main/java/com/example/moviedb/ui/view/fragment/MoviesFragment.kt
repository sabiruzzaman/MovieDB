package com.example.moviedb.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviedb.data.source.local.MovieDbDao
import com.example.moviedb.databinding.FragmentMoviesBinding
import com.example.moviedb.paging.LoaderAdapter
import com.example.moviedb.ui.adapter.MoviesAdapter
import com.example.moviedb.ui.view.fragment.detail.DetailsActivity
import com.example.moviedb.ui.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesAdapter.MovieListener {

    private lateinit var binding: FragmentMoviesBinding
    private val viewModel by activityViewModels<MovieViewModel>()
    private lateinit var adapter: MoviesAdapter

    @Inject
    lateinit var dao: MovieDbDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        viewInit()
        return binding.root
    }

    private fun viewInit() {
        adapter = MoviesAdapter(this, dao)

        binding.progressBar.visibility = VISIBLE
        binding.moviesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.moviesRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
        movieListSubscriber()

    }

    private fun movieListSubscriber() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.movieListViewModelLiveData.observe(viewLifecycleOwner) {
                adapter.submitData(lifecycle, it)
                binding.progressBar.visibility = GONE
            }
        }
    }

    override fun movieItemClick(movieId: Int) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("id", movieId)
        startActivity(intent)
    }

}