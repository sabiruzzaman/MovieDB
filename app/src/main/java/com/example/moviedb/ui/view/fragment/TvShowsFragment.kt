package com.example.moviedb.ui.view.fragment

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
import com.example.moviedb.databinding.FragmentTvShowsBinding
import com.example.moviedb.paging.LoaderAdapter
import com.example.moviedb.ui.adapter.TvShowAdapter
import com.example.moviedb.ui.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TvShowsFragment : Fragment() {

    private lateinit var binding: FragmentTvShowsBinding
    private val viewModel by activityViewModels<MovieViewModel>()
    private lateinit var adapter: TvShowAdapter

    @Inject
    lateinit var dao: MovieDbDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTvShowsBinding.inflate(inflater, container, false)
        viewInit()
        return binding.root
    }


    private fun viewInit() {

        adapter = TvShowAdapter(dao)
        binding.progressBar.visibility = VISIBLE
        binding.tvShowRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.tvShowRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
        tvShowListSubscriber()

    }

    private fun tvShowListSubscriber() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.tvShowsListViewModelLiveData.observe(viewLifecycleOwner) {
                adapter.submitData(lifecycle, it)
                binding.progressBar.visibility = GONE
            }
        }
    }

}