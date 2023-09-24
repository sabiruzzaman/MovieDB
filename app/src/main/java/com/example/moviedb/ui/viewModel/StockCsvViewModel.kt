package com.example.moviedb.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.model.StockCvsData
import com.example.moviedb.data.repository.StockCsvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StockCsvViewModel @Inject constructor(
    private val stockCsvRepository: StockCsvRepository
) : ViewModel() {

    fun getStockCsvData(): List<StockCvsData> {
        return stockCsvRepository.getStockCsvData()
    }
}
