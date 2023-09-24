package com.example.moviedb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.moviedb.data.source.remote.ApiInterface
import com.example.moviedb.paging.MoviesPagingSource
import com.example.moviedb.paging.RelatedMoviesPagingSource
import com.example.moviedb.paging.TvShowsPagingSource
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val apiInterface: ApiInterface) {

    // movies
    fun moviesRepository() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { MoviesPagingSource(apiInterface) }
    ).liveData

    // Tv Shows
    fun tvShowsRepository() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { TvShowsPagingSource(apiInterface) }
    ).liveData

    //related movies
    fun relatedMovieRepository(movieId: Int) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { RelatedMoviesPagingSource(movieId, apiInterface) }
    ).liveData

    //details movies
    suspend fun detailsMovieRepository(movieId: Int) = apiInterface.getDetails(movieId)


}