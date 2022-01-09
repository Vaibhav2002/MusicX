package dev.vaibhav.musicx.data.remote.dataSource.lyrics

import dev.vaibhav.musicx.data.models.remote.lyrics.LyricsResponse
import dev.vaibhav.musicx.utils.Resource

interface LyricDataSource {

    suspend fun getLyrics(query: String): Resource<LyricsResponse>
}
