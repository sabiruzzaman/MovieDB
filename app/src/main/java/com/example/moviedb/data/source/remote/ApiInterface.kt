package com.example.moviedb.data.source.remote

import com.example.moviedb.data.model.DetailsResponse
import com.example.moviedb.data.model.MovieListResponse
import com.example.moviedb.data.model.RelatedMovieListResponse
import com.example.moviedb.data.model.TvShowListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("trending/movie/day")
    suspend fun getMovies(@Query("page") page: Int): MovieListResponse

    @GET("trending/tv/day")
    suspend fun getTvShows(@Query("page") page: Int): TvShowListResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRelatedMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ): RelatedMovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getDetails(@Path("movie_id") movieId: Int): Response<DetailsResponse>
}