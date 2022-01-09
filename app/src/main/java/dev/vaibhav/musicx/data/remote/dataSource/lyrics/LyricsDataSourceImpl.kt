package dev.vaibhav.musicx.data.remote.dataSource.lyrics

import dev.vaibhav.musicx.data.models.remote.lyrics.LyricsResponse
import dev.vaibhav.musicx.data.remote.retrofit.LyricsApi
import dev.vaibhav.musicx.utils.Resource
import dev.vaibhav.musicx.utils.safeApiCall
import javax.inject.Inject

class LyricsDataSourceImpl @Inject constructor(private val api: LyricsApi) : LyricDataSource {
    override suspend fun getLyrics(query: String): Resource<LyricsResponse> = safeApiCall {
        api.getLyrics(query)
    }
}
