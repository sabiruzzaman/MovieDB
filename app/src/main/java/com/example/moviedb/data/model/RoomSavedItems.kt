package com.example.moviedb.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "saved_items")
@Parcelize
data class RoomSavedItems(
    @PrimaryKey val itemId: Int,
    val title: String,
    val release_date: String,
    val poster_path: String,
    val type: Int
) : Parcelable