package com.dec.indpreleague.ui.main.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teamTable")
data class TeamEntity(
    @PrimaryKey(autoGenerate = true)
    var teamId: Int,
    @ColumnInfo(name = "teamName")
    var teamName: String? = null
)