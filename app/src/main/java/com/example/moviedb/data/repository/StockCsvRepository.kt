package com.example.moviedb.data.repository

import android.content.Context
import com.example.moviedb.R
import com.example.moviedb.data.model.StockCvsData
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class StockCsvRepository @Inject constructor(private val context: Context) {

    fun getStockCsvData(): List<StockCvsData> {
        val csvData = mutableListOf<StockCvsData>()
        val inputStream = context.resources.openRawResource(R.raw.stock)

        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?

        try {
            while (reader.readLine().also { line = it } != null) {
                val values = line?.split(",") ?: emptyList()
                if (values.size >= 2) {
                    val timestamp = values[0].toLongOrNull() ?: 0L
                    val close = values[1].toDoubleOrNull() ?: 0.0
                    csvData.add(StockCvsData(timestamp, close))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return csvData
    }
}
