package com.example.moviedb.ui.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentGraphBinding
import com.example.moviedb.ui.viewModel.StockCsvViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GraphFragment : Fragment() {

    private lateinit var binding: FragmentGraphBinding
    private val viewModel by activityViewModels<StockCsvViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        viewInit()
        return binding.root
    }

    private fun viewInit() {

        val stockCsvData = viewModel.getStockCsvData()


        val lineChart: LineChart = binding.lineChart
        val entries: ArrayList<Entry> = ArrayList()

        for ((index, dataPoint) in stockCsvData.withIndex()) {
            entries.add(Entry(index.toFloat(), dataPoint.open.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Spike Graph")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.RED)
        dataSet.setDrawValues(false) // Disable drawing values on data points
        dataSet.setDrawFilled(true) // Fill the area under the line
        dataSet.fillDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.gradient_fill
        ) // Use a gradient fill

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()


    }


}