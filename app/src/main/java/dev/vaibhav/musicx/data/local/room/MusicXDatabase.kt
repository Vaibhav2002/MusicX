package dev.vaibhav.musicx.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.vaibhav.musicx.data.models.local.Music

@Database(entities = [Music::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MusicXDatabase : RoomDatabase() {

    abstract fun getMusicDao(): MusicDao
}
