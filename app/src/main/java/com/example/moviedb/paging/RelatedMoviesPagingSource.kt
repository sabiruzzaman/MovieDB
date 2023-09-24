package com.example.moviedb.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviedb.data.model.MovieListResponse
import com.example.moviedb.data.model.RelatedMovieListResponse
import com.example.moviedb.data.model.TvShowListResponse
import com.example.moviedb.data.source.remote.ApiInterface

class RelatedMoviesPagingSource(private val movieId: Int, private val movieApi: ApiInterface) :
    PagingSource<Int, RelatedMovieListResponse.Results>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RelatedMovieListResponse.Results> {

        try {

            val position = params.key ?: 1
            val response = movieApi.getRelatedMovies(movieId = movieId, position)

            // Check if response.results is null or empty
            if (response.results.isNullOrEmpty()) {
                // Return an error indicating no data available
                return LoadResult.Error(Exception("No related movies available for movieId $movieId"))
            }

            return LoadResult.Page(
                data = response.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.total_pages) null else position + 1
            )


        } catch (e: Exception) {
            return LoadResult.Error(e)
        }


    }

    override fun getRefreshKey(state: PagingState<Int, RelatedMovieListResponse.Results>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}