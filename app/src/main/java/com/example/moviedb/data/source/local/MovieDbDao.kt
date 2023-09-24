package com.example.moviedb.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.moviedb.data.model.RoomSavedItems

@Dao
interface MovieDbDao {

    @Query("DELETE FROM saved_items WHERE itemId=:itemId")
    fun deleteFromSaved(itemId: Int)

    @Insert
    fun addItemToSaved(item: RoomSavedItems)

    @Query("SELECT COUNT(*) FROM saved_items WHERE itemId = :itemId")
    fun isItemInSaved(itemId: Int): Boolean

    @Query("SELECT * FROM saved_items")
    fun getAllSavedItem(): List<RoomSavedItems>
}