package dev.vaibhav.musicx.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_table")
data class Music(
    @PrimaryKey
    val id: String,
    val title: String,
    val duration: Long,
    val artists: List<String>,
    val imageUrl: String,
    val musicUrl: String
)
