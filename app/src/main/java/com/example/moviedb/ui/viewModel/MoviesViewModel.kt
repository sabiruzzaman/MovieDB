package com.example.moviedb.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviedb.data.model.DetailsResponse
import com.example.moviedb.data.model.MovieListResponse
import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.utils.NetworkResult
import com.example.moviedb.utils.NoInternetException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MoviesRepository
) : ViewModel() {

    private var currentMovieId: Int = 0

    val movieListViewModelLiveData = movieRepository.moviesRepository().cachedIn(viewModelScope)


    val tvShowsListViewModelLiveData = movieRepository.tvShowsRepository().cachedIn(viewModelScope)

    fun relatedMovieViewModelLiveData(movieId: Int) =
        movieRepository.relatedMovieRepository(movieId).cachedIn(viewModelScope)

    private var _detailsMovieViewModelLiveData = MutableLiveData<NetworkResult<DetailsResponse>>()
    val movieDetails: LiveData<NetworkResult<DetailsResponse>> get() = _detailsMovieViewModelLiveData

    fun getDetailsMovieViewModel(movieId: Int) {
        _detailsMovieViewModelLiveData.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            try {
                val response = movieRepository.detailsMovieRepository(movieId)

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _detailsMovieViewModelLiveData.postValue(NetworkResult.Success(responseBody))
                    } else {
                        // Handle the case where the response body is null
                        _detailsMovieViewModelLiveData.postValue(NetworkResult.Error("Response body is null"))
                    }
                } else {
                    // Handle unsuccessful responses (e.g., HTTP error codes)
                    val errorObj = JSONObject(response.errorBody()?.charStream()?.readText() ?: "")
                    _detailsMovieViewModelLiveData.postValue(
                        NetworkResult.Error(
                            errorObj.getString(
                                "message"
                            )
                        )
                    )
                }
            } catch (noInternetException: NoInternetException) {
                _detailsMovieViewModelLiveData.postValue(
                    NetworkResult.Error(
                        noInternetException.localizedMessage ?: "No internet connection"
                    )
                )
            } catch (e: Exception) {
                // Handle other exceptions
                _detailsMovieViewModelLiveData.postValue(
                    NetworkResult.Error(
                        e.localizedMessage ?: "An error occurred"
                    )
                )
            }
        }
    }


//    private var _detailsMovieViewModelLiveData = MutableLiveData<NetworkResult<DetailsResponse>>()
//    val movieDetails: LiveData<NetworkResult<DetailsResponse>> get() = _detailsMovieViewModelLiveData
//
//    fun getDetailsMovieViewModel(movieId: Int) {
//        _detailsMovieViewModelLiveData.postValue(NetworkResult.Loading())
//        viewModelScope.launch {
//
//            try {
//                val response = movieRepository.detailsMovieRepository(movieId)
//
//                if (response.isSuccessful && response.body() != null) {
//                    _detailsMovieViewModelLiveData.postValue(NetworkResult.Success(response.body()!!))
//
//                } else if (response.errorBody() != null) {
//                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//                    _detailsMovieViewModelLiveData.postValue(
//                        NetworkResult.Error(
//                            errorObj.getString(
//                                "message"
//                            )
//                        )
//                    )
//
//                }
//            } catch (noInternetException: NoInternetException) {
//                _detailsMovieViewModelLiveData.postValue(noInternetException.localizedMessage?.let {
//                    NetworkResult.Error(
//                        it
//                    )
//                })
//            }
//        }
//    }
}


