package dev.vaibhav.musicx.data.local.room

import androidx.room.*
import dev.vaibhav.musicx.data.models.local.Music
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Query("SELECT * FROM music_table WHERE title LIKE '%' || :query || '%' ")
    fun getAllSongsFlow(query: String): Flow<List<Music>>

    @Query("SELECT * FROM music_table")
    suspend fun getAllSongs(): List<Music>

    @Query("SELECT * FROM music_table WHERE id = :id")
    suspend fun getSongById(id: String): Music

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(songs: List<Music>)

    @Query("DELETE FROM music_table")
    suspend fun deleteAllSongs()

    @Delete
    suspend fun deleteAllSongs(song: Music)
}
