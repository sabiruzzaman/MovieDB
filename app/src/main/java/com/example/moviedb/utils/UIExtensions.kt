package com.example.moviedb.utils

import android.content.Context
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


fun String.datePrettier(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

    try {
        val date = inputFormat.parse(this)
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this
}


fun Any.convertMinutesToHoursAndMinutes(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return String.format("%02dh:%02dm", hours, remainingMinutes)
}


fun Any.convertToCurrency(input: Int): String {
    return try {
        val value = input.toDouble()
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.format(value)
    } catch (e: NumberFormatException) {
        // Handle invalid input here
        "Invalid input"
    }
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

