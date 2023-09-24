package com.example.moviedb.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviedb.data.model.TvShowListResponse
import com.example.moviedb.data.source.remote.ApiInterface

class TvShowsPagingSource(private val movieApi: ApiInterface) :
    PagingSource<Int, TvShowListResponse.Results>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShowListResponse.Results> {

        return try {

            val position = params.key ?: 1
            val response = movieApi.getTvShows(position)
            LoadResult.Page(
                data = response.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.total_pages) null else position + 1
            )


        } catch (e: Exception) {
            LoadResult.Error(e)
        }


    }

    override fun getRefreshKey(state: PagingState<Int, TvShowListResponse.Results>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}