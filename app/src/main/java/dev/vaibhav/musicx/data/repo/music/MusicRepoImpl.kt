package dev.vaibhav.musicx.data.repo.music

import dev.vaibhav.musicx.data.local.dataSource.RoomMusicDataSource
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.data.models.mapper.MusicMapper
import dev.vaibhav.musicx.data.remote.dataSource.music.MusicDataSource
import dev.vaibhav.musicx.utils.Resource
import dev.vaibhav.musicx.utils.mapToUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MusicRepoImpl @Inject constructor(
    private val musicDataSource: MusicDataSource,
    private val musicDb: RoomMusicDataSource,
    private val musicMapper: MusicMapper
) : MusicRepo {

    override fun getAllSongsFlow(query: String) = musicDb.getAllSongsFlow(query)

    override suspend fun fetchAllMusic(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val musicResource = musicDataSource.getAllMusic()
        if (musicResource is Resource.Success)
            musicResource.data?.let { cacheMusic(musicMapper.toDomainList(it)) }
        emit(musicResource.mapToUnit())
    }

    override suspend fun uploadSong(music: Music): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val musicResource = musicDataSource.uploadMusic(musicMapper.toNetwork(music))
        emit(musicResource)
    }

    override suspend fun getAllSongs() = musicDb.getAllSongs()

    override suspend fun insertSongs(songs: List<Music>) = musicDb.insertSong(songs)

    override suspend fun deleteSong(song: Music) = musicDb.deleteSong(song)

    override suspend fun deleteAllSongs() = musicDb.deleteAllSongs()

    override suspend fun getSongById(id: String) = musicDb.getSongById(id)

    override suspend fun cacheMusic(songs: List<Music>) {
        deleteAllSongs()
        insertSongs(songs)
    }
}
