package com.dec.indpreleague.ui.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TeamDao {
    @Query("SELECT * FROM teamTable")
    fun getAll(): List<TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(team: TeamEntity)
}