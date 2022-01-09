package dev.vaibhav.musicx.exoplayer.datasource

import dev.vaibhav.musicx.data.local.room.MusicDao
import dev.vaibhav.musicx.data.models.mapper.MusicMapper
import dev.vaibhav.musicx.data.remote.dataSource.music.MusicDataSource
import dev.vaibhav.musicx.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicPlayerDataSource @Inject constructor(
    private val dataSource: MusicDataSource,
    private val musicMapper: MusicMapper,
    private val musicDao: MusicDao
) : MusicPlayerDataSource() {

    override suspend fun getMusic() = withContext(Dispatchers.IO) {
        state = State.STATE_INITIALIZING
        val musicResource = dataSource.getAllMusic()
        val musicDTOList = if (musicResource is Resource.Success) {
            musicResource.data ?: emptyList()
        } else emptyList()
        allMusic = musicMapper.toDomainList(musicDTOList)
        musicDao.insertSong(allMusic)
        state = State.STATE_INITIALIZED
    }
}
