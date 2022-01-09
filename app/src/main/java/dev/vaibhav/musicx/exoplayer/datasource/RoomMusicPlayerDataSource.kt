package dev.vaibhav.musicx.exoplayer.datasource

import dev.vaibhav.musicx.data.local.room.MusicDao
import dev.vaibhav.musicx.data.models.mapper.MusicMapper
import dev.vaibhav.musicx.data.remote.dataSource.music.MusicDataSource
import dev.vaibhav.musicx.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomMusicPlayerDataSource @Inject constructor(
    private val dataSource: MusicDataSource,
    private val musicMapper: MusicMapper,
    private val musicDao: MusicDao
) : MusicPlayerDataSource() {

    override suspend fun getMusic() = withContext(Dispatchers.IO) {
        state = State.STATE_INITIALIZING
        dataSource.getAllMusic().also {
            if (it is Resource.Success)
                musicDao.insertSong(musicMapper.toDomainList(it.data ?: emptyList()))
        }
        allMusic = musicDao.getAllSongs()
        state = State.STATE_INITIALIZED
    }
}
