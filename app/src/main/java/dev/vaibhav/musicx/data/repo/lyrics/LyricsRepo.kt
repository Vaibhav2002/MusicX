package dev.vaibhav.musicx.data.repo.lyrics

import dev.vaibhav.musicx.data.models.local.Lyric
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LyricsRepo {

    suspend fun getLyric(song: Music): Flow<Resource<Lyric>>
}
