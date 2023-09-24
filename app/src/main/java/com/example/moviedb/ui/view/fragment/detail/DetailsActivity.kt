package com.example.moviedb.ui.view.fragment.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.moviedb.R
import com.example.moviedb.data.model.DetailsResponse
import com.example.moviedb.databinding.ActivityDetailsBinding
import com.example.moviedb.paging.LoaderAdapter
import com.example.moviedb.ui.adapter.RelatedMoviesAdapter
import com.example.moviedb.ui.viewModel.MovieViewModel
import com.example.moviedb.utils.Constants.IMG_PREFIX
import com.example.moviedb.utils.NetworkResult
import com.example.moviedb.utils.convertMinutesToHoursAndMinutes
import com.example.moviedb.utils.convertToCurrency
import com.example.moviedb.utils.datePrettier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel by viewModels<MovieViewModel>()
    private lateinit var adapter: RelatedMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater);
        setContentView(binding.root)
        viewInit()
    }

    private fun viewInit() {

        binding.backImg.setOnClickListener { finish() }
        binding.goBack.setOnClickListener { finish() }
        val currentMovieId = intent.getIntExtra("id", -1)
        viewModel.getDetailsMovieViewModel(currentMovieId)

        adapter = RelatedMoviesAdapter()
        binding.relatedRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )

        binding.progressbar.visibility = VISIBLE
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.relatedMovieViewModelLiveData(currentMovieId)
                .observe(this@DetailsActivity) {
                    adapter.submitData(lifecycle, it)
                    binding.progressbar.visibility = GONE
                }
        }



        movieDetailsObserver()
    }

    private fun movieDetailsObserver() {
        viewModel.movieDetails.observe(this@DetailsActivity) {
            when (it) {
                is NetworkResult.Error -> {
                    binding.scrollView.visibility = GONE
                    binding.noDataLayout.visibility = VISIBLE
                }

                is NetworkResult.Loading -> {
                    binding.progressbar.visibility = VISIBLE
                }

                is NetworkResult.Success -> {
                    binding.scrollView.visibility = VISIBLE
                    binding.noDataLayout.visibility = GONE
                    it.data?.let { details ->
                        setDetails(details)
                    }

                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setDetails(details: DetailsResponse) {
        binding.apply {
            binding.posterImg.load(IMG_PREFIX + details.poster_path) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                transformations(RoundedCornersTransformation())
            }

            movieName.text = details.title
            duration.text = convertMinutesToHoursAndMinutes(details.runtime)
            ratting.text = "${details.vote_average} IMDb"

            if (details.release_date != "") {
                movieReleaseDate.text = details.release_date.datePrettier()
            }
            genreOfMovie.text = details.genres[0].name
            movieDetails.text = details.overview

            releaseStatus.text = details.status
            val lan = if (details.original_language == "en") "English" else "Others"
            language.text = lan
            budget.text = convertToCurrency(details.budget)
            revenue.text = convertToCurrency(details.revenue)


        }

    }
}