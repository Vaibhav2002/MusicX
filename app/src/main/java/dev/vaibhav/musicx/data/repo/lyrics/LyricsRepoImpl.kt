package dev.vaibhav.musicx.data.repo.lyrics

import dev.vaibhav.musicx.data.models.local.Lyric
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.data.models.mapper.LyricMapper
import dev.vaibhav.musicx.data.remote.dataSource.lyrics.LyricDataSource
import dev.vaibhav.musicx.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class LyricsRepoImpl @Inject constructor(
    private val dataSource: LyricDataSource,
    private val mapper: LyricMapper
) : LyricsRepo {

    private val fetchedLyrics = hashMapOf<String, Lyric>()

    override suspend fun getLyric(song: Music): Flow<Resource<Lyric>> = flow {
        emit(Resource.Loading())
        val resource = fetchedLyrics[song.id]?.let {
            Resource.Success(it)
        } ?: handleLyricNotExistsInCache(song)
        emit(resource)
    }

    private suspend fun handleLyricNotExistsInCache(song: Music): Resource<Lyric> {
        val modifiedSongTitle = (
            if (song.title.contains("-"))
                song.title.substring(0, song.title.indexOf('-'))
            else song.title
            ).trim()
        val query = "$modifiedSongTitle by ${song.artists.joinToString(", ")}"
        val lyricResource = dataSource.getLyrics(query)
        return if (lyricResource is Resource.Success) {
            val validDTO = lyricResource.data?.result?.find {
                it.fullTitle.contains(modifiedSongTitle) && isAnyArtistContained(it.artist, song)
            }
            Timber.d(validDTO.toString())
            validDTO?.let {
                val lyric = mapper.toDomain(it).copy(songId = song.id)
                fetchedLyrics[song.id] = lyric
                Resource.Success(lyric)
            } ?: Resource.Error(message = "No valid lyrics found")
        } else Resource.Error(errorType = lyricResource.errorType, message = lyricResource.message)
    }

    private fun isAnyArtistContained(artist: String, song: Music) = song.artists.map {
        artist.contains(it)
    }.any { it }
}
