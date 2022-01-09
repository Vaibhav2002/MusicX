package dev.vaibhav.musicx.data.remote.dataSource.music

import dev.vaibhav.musicx.data.models.remote.MusicDTO
import dev.vaibhav.musicx.utils.Resource

interface MusicDataSource {

    suspend fun getAllMusic(): Resource<List<MusicDTO>>

    suspend fun uploadMusic(music: MusicDTO): Resource<Unit>
}
